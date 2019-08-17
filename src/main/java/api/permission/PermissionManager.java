package api.permission;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class PermissionManager {
    private static final Map<String, PermissionGroup> map = new HashMap<>();
    static {
        recalculateGroup();
    }

    /**
     * Инициализация стандартных рангов
     */
    public static void recalculateGroup() {
        addGroup(PermissionGroup.DEFAULT, new PermissionBase("ПОЛЬЗОВАТЕЛЬ", ""));
        addGroup(PermissionGroup.MODER, new PermissionBase("МОДЕР", "roflanbot.settitle"));
        addGroup(PermissionGroup.ADMIN, new PermissionBase("АДМИН", "*"));
    }

    @Contract(pure = true)
    public static Map<String, PermissionGroup> getLookup() {
        return map;
    }

    public static PermissionGroup getPermGroup(String name) {
        return map.get(name);
    }

    public static final void addGroup(String name, PermissionBase base) {
        map.put(name, new PermissionGroup(base));
    }
}

