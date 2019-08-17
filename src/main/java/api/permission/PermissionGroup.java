package api.permission;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

public class PermissionGroup {
    @NonNls
    public static final String ADMIN = "admin";
    @NonNls
    public static final String MODER = "moder";
    @NonNls
    public static final String DEFAULT = "default";

    private String prefix;
    private String suffix;
    private final PermissionBase base;

    @Contract(pure = true)
    public PermissionGroup(PermissionBase base) {
        this.base = base;
    }
    @Contract(pure = true)
    public PermissionGroup(PermissionBase base, String prefix) {
        this(base);
        this.suffix = prefix;
    }
    @Contract(pure = true)
    public PermissionGroup(PermissionBase base, String prefix, String suffix) {
        this(base);
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public final boolean notPermission(String perm) {
        return base.notPermission(perm);
    }
    public final boolean hasPermission(String perm) {
        return base.hasPermission(perm);
    }

    @Contract(pure = true)
    public final String getPrefix() {
        return prefix;
    }
    @Contract(pure = true)
    public final String getSuffix() {
        return suffix;
    }

    @Contract(pure = true)
    public final PermissionBase getBase() {
        return base;
    }
}
