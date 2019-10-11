package api.permission;

import api.file.JsonCustom;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import jolyjdia.bot.Loader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public final class PermissionManager extends JsonCustom implements JsonDeserializer<Map<String, PermissionGroup>> {
    @NonNls public static final String ADMIN = "admin";
    @NonNls public static final String DEFAULT = "default";
    private final Map<String, PermissionGroup> groups = new HashMap<>();
    private static PermissionManager manager;

    public PermissionManager(File file) {
        super(file);
        this.setGson(new GsonBuilder()
                .registerTypeAdapter(Map.class, this)
                .setPrettyPrinting()
                .setExclusionStrategies(new MyExclusionStrategy())
                .create());
        this.load(new MapTypeToken().getType());
    }
    public static void registerPermissionGroups() {
        manager = new PermissionManager(new File(
                Objects.requireNonNull(Loader.class.getClassLoader().getResource("permissions.json")).getFile()
        ));
        addGroup(new PermissionGroup(DEFAULT, Sets.newHashSet(""), "ПОТРИБЛЯТЬ", null));
        addGroup(new PermissionGroup(ADMIN, Sets.newHashSet("*"), "АДМИН", null));
    }
    @Contract(pure = true)
    public static PermissionManager getInstance() {
        if(manager == null) {
            throw new NullPointerException("not initialized yet");
        }
        return manager;
    }
    @Contract(pure = true)
    public static Map<String, PermissionGroup> getMapGroup() {
        return getInstance().groups;
    }

    public static PermissionGroup getPermGroup(String name) {
        return getMapGroup().get(name);
    }

    public static void addGroup(String name, String prefix, String... permissions) {
        getMapGroup().put(name, new PermissionGroup(name, Sets.newHashSet(permissions), prefix, null));
        getInstance().save(getMapGroup());
    }
    private static void addGroup(PermissionGroup group) {
        getMapGroup().put(group.getName(), group);
        getInstance().save(getMapGroup());
    }
    @Contract(pure = true)
    public static PermissionGroup getDefault() {
        return getMapGroup().get(DEFAULT);
    }

    /**
     * @param jsonElement
     * @param type
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Override
    public Map<String, PermissionGroup> deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject obj = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> keyEntry : obj.entrySet()) {
            String groupName = keyEntry.getKey();
            JsonObject object = keyEntry.getValue().getAsJsonObject();

            JsonArray permissionArray = object.getAsJsonArray("permissions");
            HashSet<String> permissions = new HashSet<>(permissionArray.size());
            for (JsonElement element : permissionArray) {
                permissions.add(element.getAsString());
            }
            String prefix = object.get("prefix").getAsString();
            JsonElement elementSuffix = object.get("suffix");
            String suffix = elementSuffix == null ? null : elementSuffix.getAsString();
            groups.put(groupName, new PermissionGroup(groupName, permissions, prefix, suffix));
        }
        return groups;
    }

    private static class MapTypeToken extends TypeToken<Map<String, PermissionGroup>> {}
}
