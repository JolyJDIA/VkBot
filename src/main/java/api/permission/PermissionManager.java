package api.permission;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PermissionManager {
    public static final String ADMIN = "admin";
    public static final String MODER = "moder";
    public static final String DEFAULT = "default";
    private static final HashMap<String, PermissionGroup> lookup = new HashMap<>();
    static {
        addGroup(DEFAULT,"ПОЛЬЗОВАТЕЛЬ");
        addGroup(MODER, "МОДЕР", "roflanbot.settitle");
        addGroup(ADMIN, "АДМИН", "*");
    }
    @Contract(pure = true)
    public static Map<String, PermissionGroup> getLookup() {
        return lookup;
    }
    public static PermissionGroup getDefault() {
        return lookup.get(DEFAULT);
    }

    public static PermissionGroup getPermGroup(String name) {
        return lookup.get(name);
    }

    public static final void addGroup(String name, String prefix, String... permissions) {
        final PermissionGroup group = new PermissionGroup(name, Sets.newHashSet(permissions));
        group.setSuffix(prefix);
        lookup.put(name, group);
    }
    public static final void addGroup(String name, String prefix, HashSet<String> permission) {
        final PermissionGroup group = new PermissionGroup(name, permission);
        group.setSuffix(prefix);
        lookup.put(name, group);
    }
}