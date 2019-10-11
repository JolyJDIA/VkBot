package api.permission;

import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Locale;

public class PermissionGroup {
    @Expose(serialize = false, deserialize = false)
    private final String name;

    private final HashSet<String> permissions;
    private String prefix;
    private String suffix;

    @Contract(pure = true)
    public PermissionGroup(String name, HashSet<String> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
    @Contract(pure = true)
    public PermissionGroup(String name, HashSet<String> permissions, String prefix, String suffix) {
        this(name, permissions);
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Contract(pure = true)
    public final String getName() {
        return name;
    }

    public final void addPermission(String permission) {
        this.permissions.add(permission);
    }
    /**
     * Проверка на отсутствие разрешения
     * @param permission Проверяемое разрешение на доступ
     * @return true, если доступ разрешен, в противном случае false
     */
    public final boolean notPermission(String permission) {
        return !(checkStar(permission) || this.permissions.contains(permission.toLowerCase(Locale.ENGLISH)));
    }

    /**
     * Проверка разрешения
     * @param permission Проверяемое разрешение на доступ
     * @return true, если доступ разрешен, в противном случае false
     */
    public final boolean hasPermission(String permission) {
        return checkStar(permission) || this.permissions.contains(permission.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Проверяет на все права
     * @param permission Проверяемое разрешение на доступ
     * @return true, если доступ разрешен, в противном случае false
     */
    private boolean checkStar(@NotNull String permission) {
        return permission.endsWith(".*") || this.permissions.stream().anyMatch(e -> !e.isEmpty() && e.charAt(0) == '*');
    }

    /**
     * @return Множество разрешений
     */
    @Contract(pure = true)
    public final HashSet<String> getPermissions() {
        return permissions;
    }

    @Contract(pure = true)
    public final String getPrefix() {
        return prefix;
    }

    @Contract(pure = true)
    public final String getSuffix() {
        return suffix;
    }

    public final void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public final void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
