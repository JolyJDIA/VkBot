package api.storage;

import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import api.utils.cache.Cache;
import com.mysql.cj.jdbc.MysqlDataSource;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MySQL implements UserBackend {
    @NonNls private static final Logger LOGGER = Logger.getLogger(MySQL.class.getName());
    private final Connection connection;
    @NonNls private static final String INSERT_OR_UPDATE_GROUP =
            "INSERT INTO `vkbot` (`peerId`, `userId`, `group`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `group` = ?";
    @NonNls private static final String SELECT =
            "SELECT `group` FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1";
    @NonNls private static final String DELETE =
            "DELETE FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1";
    @NonNls private static final String DELETE_CHAT =
            "DELETE FROM `vkbot` WHERE `peerId` = ?";

    private final Map<Integer, ChatCacheMySQL> chats = new WeakHashMap<>();

    @Contract("_, _, _ -> new")
    public static @NotNull UserBackend of(String username, String password, @NonNls String url) {
        try {
            return new MySQL(username, password, url);
        } catch (SQLException e) {
            return new ProfileList(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));
        }
    }
    private MySQL(String username, String password, @NonNls String url) throws SQLException {
        MysqlDataSource data = new MysqlDataSource();
        data.setUser(username);
        data.setPassword(password);
        data.setUrl(url + "?useUnicode=false&characterEncoding=UTF-8&serverTimezone=UTC");
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
    }
    public Map<Integer, ChatCacheMySQL> getMap() {
        return chats;
    }
    @Override
    public void saveOrUpdateGroup(User user) {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
                String group = user.getGroup().getName();
                ps.setInt(1, user.getPeerId());
                ps.setInt(2, user.getUserId());
                ps.setString(3, group);

                ps.setString(4, group);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @Contract(pure = true)
    @Override
    public @NotNull Set<Integer> getChats() {
        return chats.keySet();
    }
    @Override
    public void setRank(int peerId, int userId, PermissionGroup rank) {
        Cache<Integer, User> map = chats.computeIfAbsent(peerId, k -> new ChatCacheMySQL(peerId)).getUsers();
        getUser(peerId, userId).orElseGet(() -> {
            User user = new User(peerId, userId);
            map.put(userId, user);
            return user;
        }).setGroup(rank);
    }
    @Override
    public void setRank(User user, PermissionGroup rank) {
        if(user == null) {
            return;
        }
        user.setGroup(rank);
    }
    public ChatCacheMySQL getChat(int peerId) {
        return chats.get(peerId);
    }
    @Override
    public @NotNull Optional<User> getUser(int peerId, int userId) {
        if(chats.containsKey(peerId)) {
            if(chats.get(peerId).getUsers().containsKey(userId)) {
                return Optional.ofNullable(chats.get(peerId).getUsers().get(userId));
            }
        }
        return Optional.empty();
    }
    private @NotNull User loadUserInCache(@NotNull User user) {
        Cache<Integer, User> users = chats.computeIfAbsent(user.getPeerId(), k -> new ChatCacheMySQL(user.getPeerId())).getUsers();
        users.put(user.getUserId(), user);
        return user;
    }
    @Override
    public @NotNull User addIfAbsentAndReturn(int peerId, int userId) {
        return getUser(peerId, userId).orElseGet(() -> {
            /**
             * 1)НЕТ ЧАТА -> ЧЕКАЮ БД
             * 2)Добавил чат, проверяю доступ к участникам, проверяю на овнера возращаю, если не админ то в бд
             */
            if(chats.containsKey(peerId) && chats.get(peerId).isOwner(peerId)) {
                User owner = new User(peerId, userId, PermissionManager.getAdmin());
                owner.setOwner(true);
                return loadUserInCache(owner);
            }
            //ИДУ ЧЕКАТЬ БД
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setInt(1, peerId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    return loadUserInCache(rs.next() ? new User(peerId, userId, rs.getString(1)) : new User(peerId, userId));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return loadUserInCache(new User(peerId, userId));
        });
    }
    @Override
    public void deleteUser(int peerId, int userId) {
        if(chats.containsKey(peerId)) {
            chats.get(peerId).getUsers().remove(userId);
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
    public void deleteChat(int peerId) {
        chats.remove(peerId);
        try (PreparedStatement ps = connection.prepareStatement(DELETE_CHAT)) {
            ps.setInt(1, peerId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveAll() {
        if (chats.isEmpty()) { return; }
        try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
            for(ChatCacheMySQL integerUserCache : chats.values()) {
                for(User user : integerUserCache.getUsers().values()) {
                    if(user.unchanged()) {
                        continue;
                    }
                    String group = user.getGroup().getName();
                    ps.setInt(1, user.getPeerId());
                    ps.setInt(2, user.getUserId());
                    ps.setString(3, group);

                    ps.setString(4, group);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
