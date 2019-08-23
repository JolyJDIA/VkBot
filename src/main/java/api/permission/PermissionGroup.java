package api.permission;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Locale;

public class PermissionGroup {
    @NonNls public static final String ADMIN = "admin";
    @NonNls public static final String MODER = "moder";
    @NonNls public static final String DEFAULT = "default";

    private final HashSet<String> permission;
    private String prefix;
    private String suffix;

    public PermissionGroup(String... permission) {
        this.permission = Sets.newHashSet(permission);
    }
    @Contract(pure = true)
    public PermissionGroup(HashSet<String> permission) {
        this.permission = permission;
    }
    public PermissionGroup(String prefix, String... permission) {
        this(permission);
        this.suffix = prefix;
    }
    public PermissionGroup(String prefix, String suffix, String... permission) {
        this(permission);
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Contract(pure = true)
    public PermissionGroup(String prefix, HashSet<String> permission) {
        this(permission);
        this.suffix = prefix;
    }

    @Contract(pure = true)
    public PermissionGroup(String prefix, String suffix, HashSet<String> permission) {
        this(permission);
        this.prefix = prefix;
        this.suffix = suffix;
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
}
