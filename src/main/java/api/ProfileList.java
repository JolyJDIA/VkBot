package api;

import api.entity.User;
import api.file.FileCustom;
import api.module.Reload;
import api.permission.PermissionGroup;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ProfileList extends FileCustom implements Reload, JsonDeserializer<Map<Integer, Map<Integer, User>>> {
    private final Gson gson;
    private final Map<Integer, Map<Integer, User>> map = new HashMap<>();

    public ProfileList(@NotNull File file) {
        super(file);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.length() == 0) {
            this.create();
        }
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Map.class, this)
                .setPrettyPrinting()
                .create();
        this.load();
    }

    @Override
    public void reload() {
        this.save();
        this.map.clear();
        this.load();
    }

    @NotNull
    @Contract(pure = true)
    public Set<Integer> getChats() {
        return map.keySet();
    }

    @Override
    public void create() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print("{");
            pw.print("}");
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try (FileInputStream fileInputStream = new FileInputStream(getFile());
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            this.gson.fromJson(inputStreamReader, new ProfileList.MapTypeToken().getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print(gson.toJson(map));
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean hasUser(int peerId, int userId) {
        return map.containsKey(peerId) && map.get(peerId).containsKey(userId);
    }

    private User getUser(int peerId, int userId) {
        return map.get(peerId).get(userId);
    }

    public User add(int peerId, int userId, String group) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        User user;
        if (users.containsKey(userId)) {
            user = users.get(userId).setGroup(group);
        } else {
            user = new User(peerId, userId, group);
            users.put(userId, user);
        }
        this.save();
        return user;
    }

    public void setRank(int peerId, int userId, String rank) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        if (users.containsKey(userId)) {
            users.get(userId).setGroup(rank);
        } else {
            users.put(userId, new User(peerId, userId, rank));
        }
        this.save();
    }

    public void setPrefix(int peerId, int userId, String prefix) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        if (users.containsKey(userId)) {
            users.get(userId).setPrefix(prefix);
        } else {
            users.put(userId, new User(peerId, userId, PermissionGroup.DEFAULT, prefix));
        }
        this.save();
    }

    public void setSuffix(int peerId, int userId, String suffix) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        if (users.containsKey(userId)) {
            users.get(userId).setSuffix(suffix);
        } else {
            users.put(userId, new User(peerId, userId, PermissionGroup.DEFAULT).setSuffix(suffix));
        }
        this.save();
    }

    public void remove(int peerId, int userId) {
        if (!map.containsKey(peerId)) {
            return;
        }
        map.get(peerId).remove(userId);
        this.save();
    }

    public User get(int peerId, int userId) {
        if (!this.hasUser(peerId, userId)) {
            return this.add(peerId, userId, PermissionGroup.DEFAULT);
        }
        return this.getUser(peerId, userId);
    }

    /**
     * @param jsonElement
     * @param type
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Override
    public Map<Integer, Map<Integer, User>> deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject obj = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> keyEntry : obj.entrySet()) {
            int chat = Integer.parseInt(keyEntry.getKey());
            Map<Integer, User> users = new HashMap<>();
            JsonObject object = keyEntry.getValue().getAsJsonObject();

            for (Map.Entry<String, JsonElement> valueEntry : object.entrySet()) {
                JsonObject element = valueEntry.getValue().getAsJsonObject();
                int id = Integer.parseInt(valueEntry.getKey());
                String group = element.get("group").getAsString();
                String prefix = element.get("prefix").getAsString();
                String suffix = element.get("suffix").getAsString();
                users.put(id, new User(chat, id, group, prefix, suffix));
            }
            map.put(chat, users);
        }
        return map;
    }

    private static class MapTypeToken extends TypeToken<Map<Integer, Map<Integer, User>>> {
    }
}
