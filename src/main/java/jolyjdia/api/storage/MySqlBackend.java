package jolyjdia.api.storage;

import com.mysql.cj.jdbc.MysqlDataSource;
import jolyjdia.api.permission.PermissionManager;
import jolyjdia.api.utils.cache.Cache;
import jolyjdia.api.utils.cache.CacheBuilder;
import jolyjdia.api.utils.cache.RemovalListener;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MySqlBackend implements UserBackend {
    @NonNls private static final Logger LOGGER = Logger.getLogger(MySqlBackend.class.getName());
    private final Connection connection;
    @NonNls private static final String INSERT_OR_UPDATE_GROUP =
            "INSERT INTO `vkbot` (`peerId`, `userId`, `group`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `group` = ?";
    @NonNls private static final String SELECT =
            "SELECT `group` FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1";
    @NonNls private static final String DELETE =
            "DELETE FROM `vkbot` WHERE `peerId` = ? AND `userId` = ? LIMIT 1";
    @NonNls private static final String DELETE_CHAT =
            "DELETE FROM `vkbot` WHERE `peerId` = ?";

    private final Map<Integer, TemporaryCache> chats = new HashMap<>();
    public static Set<Integer> CHATS = new HashSet<>();

    public static @NotNull UserBackend of(String username, String password, @NonNls String url) {
        try {
            return new MySqlBackend(username, password, url);
        } catch (SQLException e) {
            return new JsonBackend(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));
        }
    }
    private MySqlBackend(String username, String password, @NonNls String url) throws SQLException {
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
                    PRIMARY KEY (`peerId`, `userId`))
                    CHARACTER SET utf8 COLLATE utf8_general_ci
                    """);
        }
        LOGGER.log(Level.INFO, "MySQL Connected!");
    }
    @Override
    public void saveOrUpdateGroup(User user) {
        Bot.getScheduler().runAsyncTask(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
                String group = user.getGroup().getName();
                ps.setInt(1, user.getChat().getPeerId());
                ps.setInt(2, user.getUserId());
                ps.setString(3, group);

                ps.setString(4, group);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public @NotNull Set<Integer> getChats() {
        return chats.keySet();
    }

    @Override
    public TemporaryCache getChatAndPutIfAbsent(int peerId) {
        CHATS.add(peerId);
        return chats.computeIfAbsent(peerId, k -> new TemporaryCache(peerId));
    }
    @Override
    public @NotNull Optional<User> getUser(int peerId, int userId) {
        TemporaryCache chat = getChatAndPutIfAbsent(peerId);
        if(chats.containsKey(peerId) && chat.getUsers().containsKey(userId)) {
            return Optional.of(chat.getUsers().get(userId));
        }
        return Optional.empty();
    }
    @NotNull
    @Contract("_ -> param1")
    private User loadUserInCache(@NotNull User user) {
        int peerId = user.getChat().getPeerId();
        chats.get(peerId).getUsers().put(user.getUserId(), user);
        return user;
    }
    @Override
    public User addIfAbsentAndReturnUser(int peerId, int userId) {
        return getUser(peerId, userId).orElseGet(() -> {
            if(Chat.isOwner(peerId, userId)) {
                User owner = new User(peerId, userId, PermissionManager.getAdmin());
                owner.setOwner(true);
                return loadUserInCache(owner);
            }
            //Async
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
            /*try {
                return Bot.getScheduler().submitAsync(() -> {
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
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }*/
        });
    }
    @Override
    public void deleteUser(int peerId, int userId) {
        if(chats.containsKey(peerId)) {
            getChatAndPutIfAbsent(peerId).getUsers().remove(userId);
        }
        Bot.getScheduler().runAsyncTask(() -> {
            try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
                ps.setInt(1, peerId);
                ps.setInt(2, userId);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteChat(int peerId) {
        chats.get(peerId).getUsers().stop();
        chats.remove(peerId);
        Bot.getScheduler().runAsyncTask(() -> {
            try (PreparedStatement ps = connection.prepareStatement(DELETE_CHAT)) {
                ps.setInt(1, peerId);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void saveAll() {
        if (chats.isEmpty()) { return; }
        Bot.getScheduler().runAsyncTask(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
                chats.values().forEach(e -> e.getUsers().values().stream().filter(User::isChange).forEach(u -> {
                    String group = u.getGroup().getName();
                    try {
                        ps.setInt(1, u.getChat().getPeerId());
                        ps.setInt(2, u.getUserId());
                        ps.setString(3, group);

                        ps.setString(4, group);
                        ps.addBatch();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }));
                ps.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private static class TemporaryCache extends Chat<Cache<Integer, User>> {

        protected TemporaryCache(int peerId) {
            super(CacheBuilder.newBuilder()
                    .expireAfterWrite(45, TimeUnit.MINUTES)//45
                    .ticker(3)
                    .removeListener((RemovalListener<Integer, User>) entry -> {
                        User user = entry.getValue();
                        if (user.isChange()) {
                            Bot.getUserBackend().saveOrUpdateGroup(user);
                        }
                    }).build(), peerId);
        }
    }
    /**public static class CacheMap extends WeakHashMap<Integer, TemporaryCache> {
        @Override
        public final boolean remove(Object key, Object value) {
            get(key).getUsers().stop();
            return super.remove(key, value);
        }
    }*/
}
