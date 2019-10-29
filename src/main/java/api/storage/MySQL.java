package api.storage;

import api.Bot;
import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Loader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MySQL implements UserBackend {
    private Connection connection;
    @NonNls private static final String INSERT_OR_UPDATE_GROUP =
            "INSERT INTO `vkbot` (`peerId`, `userId`, `group`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `group` = ?;";
    @NonNls private static final String SELECT =
            "SELECT * FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1;";

    private final Map<Integer, Cache<Integer, User>> chats = Maps.newHashMap();
    public MySQL(String username, String password, @NonNls String url) {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            MysqlDataSource data = new MysqlDataSource();
            data.setUser(username);
            data.setPassword(password);
            data.setUrl(url + "?useUnicode=true&characterEncoding=UTF-8");
            try {
                this.connection = data.getConnection();
                try (Statement statement = connection.createStatement()) {
                    statement.execute("""
                    CREATE TABLE IF NOT EXISTS `vkbot` (
                    `peerId` INT(36) NOT NULL,
                    `userId` INT(36) NOT NULL,
                    `group` VARCHAR(16) NOT NULL,
                    PRIMARY KEY (`userId`, `peerId`))
                    CHARACTER SET utf8 COLLATE utf8_general_ci
                    """);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private final void saveOrUpdateGroup(User user) {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
                ps.setInt(1, user.getPeerId());
                ps.setInt(2, user.getUserId());
                ps.setString(3, user.getGroup().getName());

                ps.setString(4, user.getGroup().getName());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @Contract(pure = true)
    public final @NotNull Set<Integer> getChats() {
        return chats.keySet();
    }
    public final void setRank(int peerId, int userId, PermissionGroup rank) {
        addIfAbsentAndConsumer(new User(peerId, userId), user -> user.setGroup(rank));
    }
    public final void setRank(User user, PermissionGroup rank) {
        if(user == null) {
            return;
        }
        addIfAbsentAndConsumer(user, user1 -> user1.setGroup(rank));
    }
    private boolean hasUser(@NotNull User user) {
        return chats.containsKey(user.getPeerId()) && chats.get(user.getPeerId()).asMap().containsKey(user.getUserId());
    }
    private boolean hasUser(int peerId, int userId) {
        return chats.containsKey(peerId) && chats.get(peerId).asMap().containsKey(userId);
    }
    public @Nullable User getUser(@NotNull User user) {
        if (hasUser(user)) {
            return chats.get(user.getPeerId()).getIfPresent(user.getUserId());
        }
        return null;
    }

    @Override
    public final @Nullable User getUser(int peerId, int userId) {
        if (hasUser(peerId, userId)) {
            return chats.get(peerId).getIfPresent(userId);
        }
        return null;
    }

    private void addIfAbsentAndConsumer(@NotNull User user, @NotNull Consumer<? super User> consumer) {
        Cache<Integer, User> users = chats.computeIfAbsent(user.getPeerId(), k -> CacheBuilder.newBuilder()
                .maximumSize(20)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build());
        int userId = user.getUserId();
        if(users.asMap().containsKey(userId)) {
            consumer.accept(users.getIfPresent(userId));
        } else {
            consumer.accept(user);
            users.put(userId, user);
        }
        saveOrUpdateGroup(user);
    }
    @Contract("_ -> param1")
    private final @NotNull User loadUserInCache(@NotNull User user) {
        Cache<Integer, User> users = chats.computeIfAbsent(user.getPeerId(), k -> CacheBuilder.newBuilder()
                .maximumSize(20)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build());
        users.put(user.getUserId(), user);
        return user;
    }
    public final @Nullable User addIfAbsentAndReturn(int peerId, int userId) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
            ps.setInt(1, peerId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return loadUserInCache(rs.next() ?
                        new User(peerId, userId, rs.getString(3), "", "")
                        : initNewUser(peerId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static @NotNull User initNewUser(int peerId, int userId) {
        User user = new User(peerId, userId);
        try {
            if(Loader.getVkApiClient().messages().getConversationMembers(Bot.getGroupActor(), user.getPeerId()).execute().getItems()
                    .stream().anyMatch(e -> {
                        Boolean isAdmin = e.getIsAdmin();
                        return (e.getMemberId() == userId) && (isAdmin != null && isAdmin);
                    })) {
                user.setGroup(PermissionManager.getPermGroup(PermissionManager.ADMIN));
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return user;
    }
}
