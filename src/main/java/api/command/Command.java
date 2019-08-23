package api.command;

import api.entity.User;
import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import api.utils.ObedientBot;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;
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

    @Contract(pure = true)
    protected Command(String name) {
        this.name = name;
    }

    @Contract(pure = true)
    protected Command(String name, String arguments, String description) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
    }

    @Contract(pure = true)
    protected Command(String name, String description) {
        this.name = name;
        this.description = description;
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
        PermissionGroup group = PermissionManager.getPermGroup(user.getGroup());
        boolean access = group.hasPermission(permission);
        if(!access) {
            ObedientBot.sendMessage(noPermissionMessage, user.getPeerId());
        }
        return access;
    }

    /**
     * Разрешение отсутствует
     * @param user Пользователь
     * @return true Если у пользователя нет разрешения
     */
    public final boolean noPermission(User user) {
        if (permission == null || permission.isEmpty()) {
            return false;
        }
        PermissionGroup group = PermissionManager.getPermGroup(user.getGroup());
        boolean noAccess = group.notPermission(permission);
        if(noAccess) {
            ObedientBot.sendMessage(noPermissionMessage, user.getPeerId());
        }
        return noAccess;
    }
    @Contract(pure = true)
    @NotNull
    protected final String getUseCommand() {
        return '/' + name + (arguments != null && !arguments.isEmpty() ? ' ' + arguments : "");
    }
}
