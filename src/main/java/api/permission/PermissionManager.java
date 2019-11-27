package api.permission;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.Set;

public final class PermissionManager {
    @NonNls public static final String ADMIN = "admin";
    @NonNls public static final String DEFAULT = "default";
    private static PermissionCalculate manager;
    public static final Map<Integer, String> STAFF_ADMIN = Maps.newHashMap();
    static {
        STAFF_ADMIN.put(310289867, "Завр");
        STAFF_ADMIN.put(526616439, "Валера");
        STAFF_ADMIN.put(190345817, "Юджин");
        STAFF_ADMIN.put(526212430, "Изи мама");
        STAFF_ADMIN.put(323998691, "Богардо");
        STAFF_ADMIN.put(199686399, "Алекха");
        STAFF_ADMIN.put(477425490, "Артемка");
    }
    public static void newInstance() {
        if (manager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton PermissionManager");
        }
        manager = new PermissionCalculate(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\permissions.json"));
    }
    @Contract(pure = true)
    public static PermissionCalculate getInstance() {
        if(manager == null) {
            throw new NullPointerException("not initialized yet");
        }
        return manager;
    }
    public static PermissionGroup getPermGroup(String name) {
        return getInstance().getGroup(name);
    }
    @Contract(pure = true)
    public static boolean isStaff(int userId) {
        return STAFF_ADMIN.containsKey(userId);
    }
    public static void addGroup(String name, String prefix, String... permissions) {
        getInstance().addGroup(name, prefix, permissions);
    }
    private static void addGroup(PermissionGroup group) {
        getInstance().addGroup(group);
    }
    @Contract(pure = true)
    public static PermissionGroup getDefault() {
        return getInstance().getGroup(DEFAULT);
    }
    @Contract(pure = true)
    public static PermissionGroup getAdmin() {
        return getInstance().getGroup(ADMIN);
    }
    public static @NotNull Set<String> ranks() {
        return getInstance().ranks();
    }
}
