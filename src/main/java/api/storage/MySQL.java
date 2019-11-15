package api.storage;

import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
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
            "DELETE FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1";
    @NonNls private static final String DELETE_CHAT =
            "DELETE FROM `vkbot` WHERE `peerId` = ?";

    private final Map<Integer, Cache<Integer, User>> chats = new WeakHashMap<>();//Хз, зачем) На всякий)

    public MySQL(String username, String password, @NonNls String url) {
        MysqlDataSource data = new MysqlDataSource();
        data.setUser(username);
        data.setPassword(password);
        data.setUrl(url + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        try {
            this.connection = data.getConnection();
            try (Statement statement = connection.createStatement()) {
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS `vkbot` (
                    `peerId` INT(36) UNSIGNED NOT NULL,
                    `userId` INT(36) UNSIGNED NOT NULL,
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
    public final void saveOrUpdateGroup(User user) {
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
        Cache<Integer, User> map = chats.computeIfAbsent(peerId, k -> CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build());
        User user = map.getIfPresent(userId);
        if(user == null) {
            user = new User(peerId, userId);
            map.put(userId, user);
        }
        user.setGroup(rank);
        saveOrUpdateGroup(user);
    }
    @Override
    public final void setRank(User user, PermissionGroup rank) {
        if(user == null) {
            return;
        }
        user.setGroup(rank);
        System.out.println(user.getGroup().getName());
        saveOrUpdateGroup(user);
    }
    @Override
    public final @Nullable User getUser(int peerId, int userId) {
        if(chats.containsKey(peerId)) {
            return chats.get(peerId).getIfPresent(userId);
        }
        return null;
    }
    @Contract("_ -> param1")
    private @NotNull User loadUserInCache(@NotNull User user) {
        Cache<Integer, User> users = chats.computeIfAbsent(user.getPeerId(), k -> CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build());
        users.put(user.getUserId(), user);
        return user;
    }
    @Override
    public final @NotNull User addIfAbsentAndReturn(int peerId, int userId) {
        User user = getUser(peerId, userId);
        if(user != null) {
            return user;
        }
        User newUser = new User(peerId, userId);
        if(isOwner(peerId, userId)) {
            newUser.setGroup(PermissionManager.getAdmin());
            newUser.setOwner(true);
        }
        try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
            ps.setInt(1, peerId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    newUser.setGroup(PermissionManager.getPermGroup(rs.getString(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loadUserInCache(newUser);
    }
    @Override
    public final void deleteUser(int peerId, int userId) {
        if(chats.containsKey(peerId)) {
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

    @Override
    public final void deleteChat(int peerId) {
        chats.remove(peerId);
        try (PreparedStatement ps = connection.prepareStatement(DELETE_CHAT)) {
            ps.setInt(1, peerId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isOwner(int peerId, int userId) {
        try {
            return Bot.getVkApiClient().messages().getConversationMembers(Bot.getGroupActor(), peerId).execute().getItems().stream()
                    .filter(e -> e.getMemberId() == userId)
                    .anyMatch(e -> {
                        Boolean isOwner = e.getIsOwner();
                        Boolean isAdmin = e.getIsAdmin();
                        return (isOwner != null && isOwner) || (isAdmin != null && isAdmin);
                    });
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
