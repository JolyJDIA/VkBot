package api.permission;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

public class PermissionBase {
    private final Set<String> permission;
    @Contract(pure = true)
    public PermissionBase(String... permission) {
        this.permission = Sets.newHashSet(permission);
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
        return !checkStar(permission) || !this.permission.contains(permission.toLowerCase(Locale.ENGLISH));
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
    public final Set<String> getPermission() {
        return permission;
    }
}
