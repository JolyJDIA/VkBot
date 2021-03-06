package jolyjdia.api.command;

import com.google.common.collect.Sets;
import jolyjdia.api.storage.User;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class Command {
    @NonNls private final String name;
    private String description;
    private String arguments;
    private Set<String> alias;
    private String permission;
    private String noPermissionMessage;

    protected Command(String name) {
        this.name = name;
    }

    protected Command(String name, String description) {
        this(name);
        this.description = description;
    }

    protected Command(String name, String arguments, String description) {
        this(name, description);
        this.arguments = arguments;
    }

    /**
     * @return Название команды
     */
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
    public final String getArguments() {
        return arguments;
    }
    /**
     * @return Описание команды
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @return Множество активных псевдонимов этой команды
     */
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
        boolean hasPermission = user.hasPermission(permission);
        if(!hasPermission) {
            user.getChat().sendMessage(noPermissionMessage);
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

    protected final @NotNull String getUseCommand() {
        return '/' + name + (arguments != null && !arguments.isEmpty() ? ' ' + arguments : "");
    }
    public final boolean equalsCommand(String s2) {
        return name.equalsIgnoreCase(s2) || (alias != null && !alias.isEmpty()) && alias.stream().anyMatch(e -> e.equalsIgnoreCase(s2));
    }
}
