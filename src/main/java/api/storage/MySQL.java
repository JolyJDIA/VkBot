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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL implements UserBackend {
    @NonNls private static final Logger LOGGER = Logger.getLogger(MySQL.class.getName());
    private Connection connection;
    @NonNls private static final String INSERT_OR_UPDATE_GROUP =
            "INSERT INTO `vkbot` (`peerId`, `userId`, `group`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `group` = ?";
    @NonNls private static final String SELECT =
            "SELECT `group` FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1";
    @NonNls private static final String DELETE =
            "DELETE FROM `vkbot` WHERE `peerId` = ? AND `userId` = ?";

    private final Map<Integer, Cache<Integer, User>> chats = Maps.newHashMap();

    public MySQL(String username, String password, @NonNls String url) {
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
            LOGGER.log(Level.INFO, "MySQL Connected!");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "MySQL Connect Error: " + e + " Connection to database has failed ! Core will use flatDatabase");
            //TODO: JSON база
            e.printStackTrace();
        }
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
    @Override
    public final @NotNull Set<Integer> getChats() {
        return chats.keySet();
    }

    @Override
    public final void setRank(int peerId, int userId, PermissionGroup rank) {
        if(!hasUser(peerId, userId)) {
            return;
        }
        User user = chats.get(peerId).asMap().getOrDefault(userId, new User(peerId, userId));
        user.setGroup(rank);
        saveOrUpdateGroup(user);
    }
    @Override
    public final void setRank(User user, PermissionGroup rank) {
        if(user == null) {
            return;
        }
        user.setGroup(rank);
        saveOrUpdateGroup(user);
    }
    private boolean hasUser(@NotNull User user) {
        return chats.containsKey(user.getPeerId()) && chats.get(user.getPeerId()).asMap().containsKey(user.getUserId());
    }
    private boolean hasUser(int peerId, int userId) {
        return chats.containsKey(peerId) && chats.get(peerId).asMap().containsKey(userId);
    }
    public final @Nullable User getUser(@NotNull User user) {
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
    @Contract("_ -> param1")
    private final @NotNull User loadUserInCache(@NotNull User user) {
        Cache<Integer, User> users = chats.computeIfAbsent(user.getPeerId(), k -> CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build());
        users.put(user.getUserId(), user);
        return user;
    }
    @Override
    public final @Nullable User addIfAbsentAndReturn(int peerId, int userId) {
        if(hasUser(peerId, userId)) {
            System.out.println("ЕСТЬ");
            return chats.get(peerId).getIfPresent(userId);
        }
        try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
            ps.setInt(1, peerId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                //КЭШИРУЮ
                return loadUserInCache(rs.next() ?
                        new User(peerId, userId, rs.getString(1), "", "")
                        : initializationNewUser(peerId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public final void deleteUser(int peerId, int userId) {
        if(hasUser(peerId, userId)) {
            chats.get(peerId).invalidate(userId);
        }
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, peerId);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static @NotNull User initializationNewUser(int peerId, int userId) {
        User user = new User(peerId, userId);
        try {
            if(Loader.getVkApiClient().messages().getConversationMembers(Bot.getGroupActor(), user.getPeerId()).execute().getItems()
                    .stream().filter(e -> e.getMemberId() == userId).anyMatch(e -> {
                        Boolean isOwner = e.getIsOwner();
                        Boolean isAdmin = e.getIsAdmin();
                        return (isOwner != null && isOwner) || (isAdmin != null && isAdmin);
                    })) {
                user.setGroup(PermissionManager.getPermGroup(PermissionManager.ADMIN));
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return user;
    }
    /*
    private final void initializationChatAdmin(int peerId) {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
                for(ConversationMember member : Loader.getVkApiClient().messages()
                        .getConversationMembers(Bot.getGroupActor(), peerId)
                        .execute()
                        .getItems()) {
                    if(member.getMemberId() < 0) {
                        continue;
                    }
                    Boolean isOwner = member.getIsOwner();
                    Boolean isAdmin = member.getIsAdmin();
                    if((isOwner != null && isOwner) || (isAdmin != null && isAdmin)) {
                        ps.setInt(1, peerId);
                        ps.setInt(2, member.getMemberId());
                        ps.setString(3, PermissionManager.ADMIN);

                        ps.setString(4, PermissionManager.ADMIN);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        });
    }*/
}
