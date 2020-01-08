package jolyjdia.api.storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import jolyjdia.api.file.JsonCustom;
import jolyjdia.api.permission.PermissionManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class JsonBackend extends JsonCustom implements UserBackend,
        JsonDeserializer<Map<Integer, JsonBackend.EternalCache>>,
        JsonSerializer<Map<Integer, JsonBackend.EternalCache>> {
    private Map<Integer, EternalCache> chats = new HashMap<>();

    public JsonBackend(File file) {
        super(file);
        this.setGson(new GsonBuilder()
                .registerTypeAdapter(Map.class, this)
                .setPrettyPrinting()
                .create());
        this.load();
    }
    public void load() {
        try (FileInputStream fileInputStream = new FileInputStream(getFile());
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            this.chats = this.getGson().fromJson(inputStreamReader, new MapTypeToken().getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public Set<Integer> getChats() {
        return chats.keySet();
    }

    public boolean hasUser(int peerId, int userId) {
        return chats.containsKey(peerId) && chats.get(peerId).getUsers().containsKey(userId);
    }
    @Override
    public @NotNull Optional<User> getUser(int peerId, int userId) {
        if (hasUser(peerId, userId)) {
            return Optional.of(chats.get(peerId).getUsers().get(userId));
        }
        return Optional.empty();
    }

    @Override
    public void deleteChat(int peerId) {
        chats.remove(peerId);
    }

    @Override
    public User addIfAbsentAndReturnUser(int peerId, int userId) {
        Chat<Map<Integer, User>> users = getChatAndPutIfAbsent(peerId);
        User user;
        if (users.getUsers().containsKey(userId)) {
            user = users.getUsers().get(userId);
        } else {
            user = new User(peerId, userId);
            if(Chat.isOwner(peerId, userId)) {
                user.setGroup(PermissionManager.getPermGroup(PermissionManager.ADMIN));
                user.setOwner(true);
            }
            users.getUsers().put(userId, user);
            this.saveAll();
        }
        return user;
    }

    public void remove(@NotNull User user) {
        if (!chats.containsKey(user.getChat().getPeerId())) {
            return;
        }
        Map<Integer, User> users = chats.get(user.getChat().getPeerId()).getUsers();
        if(!users.containsKey(user.getUserId())) {
            return;
        }
        users.remove(user.getUserId());
        this.saveAll();
    }
    @Override
    public void deleteUser(int peerId, int userId) {
        if (!chats.containsKey(peerId)) {
            return;
        }
        Map<Integer, User> users = getChatAndPutIfAbsent(peerId).getUsers();
        if(!users.containsKey(userId)) {
            return;
        }
        users.remove(userId);
        this.saveAll();
    }
    @Override
    public void saveAll() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print(getGson().toJson(chats, new MapTypeToken().getType()));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EternalCache getChatAndPutIfAbsent(int peerId) {
        return chats.computeIfAbsent(peerId, k -> new EternalCache(peerId));
    }

    @Override
    public Map<Integer, EternalCache> deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject obj = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> keyEntry : obj.entrySet()) {
            int peerId = Integer.parseInt(keyEntry.getKey());
            EternalCache users = getChatAndPutIfAbsent(peerId);
            JsonObject object = keyEntry.getValue().getAsJsonObject();

            for (Map.Entry<String, JsonElement> valueEntry : object.entrySet()) {
                JsonObject element = valueEntry.getValue().getAsJsonObject();
                int id = Integer.parseInt(valueEntry.getKey());
                String group = element.get("group").getAsString();
                users.getUsers().put(id, new User(peerId, id, group));
            }
            chats.put(peerId, users);
        }
        return chats;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull Map<Integer, EternalCache> data, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for(Map.Entry<Integer, EternalCache> chat : data.entrySet()) {
            JsonObject peer = new JsonObject();
            for(Map.Entry<Integer, User> users : chat.getValue().getUsers().entrySet()) {
                JsonObject user = new JsonObject();
                User account = users.getValue();
                user.addProperty("group", account.getGroup().getName());
                peer.add(String.valueOf(users.getKey()), user);
            }
            object.add(String.valueOf(chat.getKey()), peer);
        }
        return object;
    }
    public static class EternalCache extends Chat<Map<Integer, User>> {
        protected EternalCache(int peerId) {
            super(new HashMap<>(), peerId);
        }
    }

    private static class MapTypeToken extends TypeToken<Map<Integer, Chat<Map<Integer, User>>>> {}

}
