package api.permission;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Locale;

public class PermissionGroup {
    public static final String ADMIN = "admin";
    public static final String MODER = "moder";
    public static final String DEFAULT = "default";

    private final HashSet<String> permission;
    private String prefix;
    private String suffix;

    @Contract(pure = true)
    public PermissionGroup(HashSet<String> permission) {
        this.permission = permission;
    }

    public final void addPermission(String permission) {
        this.permission.add(permission);
    }
    /**
     * Проверка на отсутствие разрешения
     * @param permission Проверяемое разрешение на доступ
     * @return true, если доступ разрешен, в противном случае false
     */
    public final boolean notPermission(String permission) {
        return !(checkStar(permission) || this.permission.contains(permission.toLowerCase(Locale.ENGLISH)));
    }

    /**
     * Проверка разрешения
     * @param permission Проверяемое разрешение на доступ
     * @return true, если доступ разрешен, в противном случае false
     */
    public final boolean hasPermission(String permission) {
        return checkStar(permission) || this.permission.contains(permission.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Проверяет на все права
     * @param permission Проверяемое разрешение на доступ
     * @return true, если доступ разрешен, в противном случае false
     */
    private boolean checkStar(@NotNull String permission) {
        return permission.endsWith(".*") || this.permission.stream().anyMatch(e -> !e.isEmpty() && e.charAt(0) == '*');
    }

    /**
     * @return Множество разрешений
     */
    @Contract(pure = true)
    public final HashSet<String> getPermission() {
        return permission;
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
