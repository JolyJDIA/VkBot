package api.permission;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class PermissionManager {
    private static final HashMap<String, PermissionGroup> lookup = new HashMap<>();
    static {
        addGroup(PermissionGroup.DEFAULT,"ПОЛЬЗОВАТЕЛЬ");
        addGroup(PermissionGroup.MODER, "МОДЕР", "roflanbot.settitle");
        addGroup(PermissionGroup.ADMIN, "АДМИН", "*");
    }
    @Contract(pure = true)
    public static Map<String, PermissionGroup> getLookup() {
        return lookup;
    }

    public static PermissionGroup getPermGroup(String name) {
        return lookup.get(name);
    }

    public static final void addGroup(String name, String prefix, String... permissions) {
        PermissionGroup group = new PermissionGroup(Sets.newHashSet(permissions));
        group.setSuffix(prefix);
        lookup.put(name, group);
    }
}

