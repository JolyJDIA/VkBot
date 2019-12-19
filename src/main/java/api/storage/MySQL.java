package api.storage;

import api.permission.PermissionManager;
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
    @Contract(pure = true)
    @Override
    public @NotNull Set<Integer> getChats() {
        return chats.keySet();
    }
    @Override
    public ChatCacheMySQL getChat(int peerId) {
        return chats.computeIfAbsent(peerId, k -> new ChatCacheMySQL(peerId));
    }
    @Override
    public @NotNull Optional<User> getUser(int peerId, int userId) {
        if(chats.containsKey(peerId) && getChat(peerId).getUsers().containsKey(userId)) {
            return Optional.of(getChat(peerId).getUsers().get(userId));
        }
        return Optional.empty();
    }
    private @NotNull User loadUserInCache(@NotNull User user) {
        int peerId = user.getChat().getPeerId();
        chats.get(peerId).getUsers().put(user.getUserId(), user);
        return user;
    }
    @Override
    public @NotNull User addIfAbsentAndReturn(int peerId, int userId) {
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
        });
    }
    @Override
    public void deleteUser(int peerId, int userId) {
        if(chats.containsKey(peerId)) {
            getChat(peerId).getUsers().remove(userId);
        }
        Bot.getScheduler().runTaskAsynchronously(() -> {
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
        chats.remove(peerId);
        Bot.getScheduler().runTaskAsynchronously(() -> {
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
        Bot.getScheduler().runTaskAsynchronously(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_OR_UPDATE_GROUP)) {
                chats.values().forEach(e -> e.getUsers().values().stream().filter(u -> !u.unchanged()).forEach(u -> {
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
}
