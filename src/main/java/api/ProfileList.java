package api;

import api.entity.User;
import api.file.FileCustom;
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

public final class ProfileList extends FileCustom implements JsonDeserializer<Map<Integer, Map<Integer, User>>> {
    private final Gson gson;
    private final HashMap<Integer, Map<Integer, User>> map = new HashMap<>();

    public ProfileList(File file) {
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

    @Contract(pure = true)
    @NotNull
    public Set<Integer> getChats() {
        return map.keySet();
    }

    @Override
    public void create() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print("{");
            pw.print("}");
            pw.flush();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
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
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean hasUser(@NotNull User user) {
        return map.containsKey(user.getPeerId()) && map.get(user.getPeerId()).containsKey(user.getUserId());
    }
    private boolean hasUser(int peerId, int userId) {
        return map.containsKey(peerId) && map.get(peerId).containsKey(userId);
    }
    private User getUser(@NotNull User user) {
        return map.get(user.getPeerId()).get(user.getUserId());
    }

    private User getUser(int peerId, int userId) {
        return map.get(peerId).get(userId);
    }

    public User addIfAbsentAndReturn(int peerId, int userId) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        User user;
        if (users.containsKey(userId)) {
            user = users.get(userId);
        } else {
            user = new User(peerId, userId);
            users.put(userId, user);
        }
        this.save();
        return user;
    }
    public void setRank(User user, String rank) {
        if(user == null) {
            return;
        }
        Map<Integer, User> users = map.computeIfAbsent(user.getPeerId(), k -> new HashMap<>());
        int userId = user.getUserId();
        if (users.containsKey(userId)) {
            users.get(userId).setGroup(rank);
        } else {
            users.put(userId, new User(user.getPeerId(), userId, rank));
        }
        this.save();
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
    public void setPrefix(User user, String prefix) {
        if(user == null) {
            return;
        }
        int userId = user.getUserId();
        Map<Integer, User> users = map.computeIfAbsent(user.getPeerId(), k -> new HashMap<>());
        if (users.containsKey(userId)) {
            users.get(userId).setPrefix(prefix);
        } else {
            users.put(userId, new User(user.getPeerId(), userId, PermissionGroup.DEFAULT, prefix));
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
    public void setSuffix(User user, String suffix) {
        if(user == null) {
            return;
        }
        int userId = user.getUserId();
        Map<Integer, User> users = map.computeIfAbsent(user.getPeerId(), k -> new HashMap<>());
        if (users.containsKey(userId)) {
            users.get(userId).setSuffix(suffix);
        } else {
            users.put(userId, new User(user.getPeerId(), userId).setSuffix(suffix));
        }
        this.save();
    }

    public void setSuffix(int peerId, int userId, String suffix) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        if (users.containsKey(userId)) {
            users.get(userId).setSuffix(suffix);
        } else {
            users.put(userId, new User(peerId, userId).setSuffix(suffix));
        }
        this.save();
    }
    public void remove(@NotNull User user) {
        if (!map.containsKey(user.getPeerId())) {
            return;
        }
        map.get(user.getPeerId()).remove(user.getUserId());
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
        return this.addIfAbsentAndReturn(peerId, userId);
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
