package api.permission;

import api.file.JsonCustom;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PermissionCalculate extends JsonCustom implements JsonDeserializer<Map<String, PermissionGroup>> {
    private final Map<String, PermissionGroup> groups = Maps.newHashMap();

    PermissionCalculate(File file) {
        super(file);
        this.setGson(new GsonBuilder()
                .registerTypeAdapter(Map.class, this)
                .setPrettyPrinting()
                .create());
        this.load(new PermissionCalculate.MapTypeToken().getType());
    }
    public final Map<String, PermissionGroup> getGroups() {
        return groups;
    }
    public final PermissionGroup getGroup(String name) {
        return groups.get(name);
    }
    public final void addGroup(String name, String prefix, String... permissions) {
        addGroup(new PermissionGroup(name, Sets.newHashSet(permissions), prefix, null));
    }
    public final void addGroup(PermissionGroup group) {
        groups.put(group.getName(), group);
        save(groups);
    }

    public final @NotNull Set<String> ranks() {
        return groups.keySet();
    }

    /**
     * @param jsonElement
     * @param type
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Override
    public final Map<String, PermissionGroup> deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext context) {
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
