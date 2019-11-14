package api.command;

import api.storage.User;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public abstract class Command {
    @NonNls private final String name;
    private String description;
    private String arguments;
    private Set<String> alias;
    private String permission;
    private String noPermissionMessage;
    protected static final Map<String, Integer> STAFF_ADMIN = Maps.newHashMap();
    static {
        STAFF_ADMIN.put("Завр", 310289867);
        STAFF_ADMIN.put("Валера", 526616439);
        STAFF_ADMIN.put("Юджин", 190345817);
        STAFF_ADMIN.put("Изи мама", 526212430);
        STAFF_ADMIN.put("Богардо", 323998691);
        STAFF_ADMIN.put("Алекха", 199686399);
    }

    @Contract(pure = true)
    protected Command(String name) {
        this.name = name;
    }

    @Contract(pure = true)
    protected Command(String name, String description) {
        this(name);
        this.description = description;
    }

    @Contract(pure = true)
    protected Command(String name, String arguments, String description) {
        this(name, description);
        this.arguments = arguments;
    }

    /**
     * @return Название команды
     */
    @Contract(pure = true)
    public final String getName() {
        return name;
    }

    /**
     * Устанавливает право на команду
     * @param permission Название права
     * @param noPermissionMessage Сообщение отказа в правах
     */
    public final void setPermission(String permission, String noPermissionMessage) {
        this.permission = permission;
        this.noPermissionMessage = noPermissionMessage;
    }

    /**
     * Выполняет команду
     * @param sender Пользователь, который ввел команду
     * @param args Аргументы команд
     */
    public abstract void execute(User sender, String[] args);

    /**
     * @return Использование команды
     */

    @Contract(pure = true)
    public final String getArguments() {
        return arguments;
    }
    /**
     * @return Описание команды
     */
    @Contract(pure = true)
    public final String getDescription() {
        return description;
    }

    /**
     * @return Множество активных псевдонимов этой команды
     */
    @Contract(pure = true)
    public final Set<String> getAlias() {
        return alias;
    }

    /**
     *
     * @param alias Множество псевдонимов для этой команды
     */
    public final void setAlias(String... alias) {
        this.alias = Sets.newHashSet(alias);
    }

    @Contract(pure = true)
    public final String getPermission() {
        return permission;
    }
    /**
     * Разрешение имеется
     * @param user Пользователь
     * @return true, если пользователь может использовать, в противном случае false
     */
    public final boolean hasPermission(User user) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        boolean hasPermission = STAFF_ADMIN.containsValue(user.getUserId()) || user.getGroup().hasPermission(permission);
        if(!hasPermission) {
            user.sendMessageFromChat(noPermissionMessage);
        }
        return hasPermission;
    }

    /**
     * Разрешение отсутствует
     * @param user Пользователь
     * @return true Если у пользователя нет разрешения
     */
    public final boolean noPermission(User user) {
        return !hasPermission(user);
    }

    @Contract(pure = true)
    protected final @NotNull String getUseCommand() {
        return '/' + name + (arguments != null && !arguments.isEmpty() ? ' ' + arguments : "");
    }
}
