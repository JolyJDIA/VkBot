package api.storage;

import api.file.JsonCustom;
import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import api.utils.VkUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public final class ProfileList extends JsonCustom implements UserBackend,
        JsonDeserializer<Map<Integer, Map<Integer, User>>>,
        JsonSerializer<Map<Integer, Map<Integer, User>>> {
    private Map<Integer, Map<Integer, User>> map = new HashMap<>();

    public ProfileList(File file) {
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
            this.map = this.getGson().fromJson(inputStreamReader, new MapTypeToken().getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull Set<Integer> getChats() {
        return map.keySet();
    }

    private boolean hasUser(@NotNull User user) {
        return map.containsKey(user.getPeerId()) && map.get(user.getPeerId()).containsKey(user.getUserId());
    }
    private boolean hasUser(int peerId, int userId) {
        return map.containsKey(peerId) && map.get(peerId).containsKey(userId);
    }
    public @Nullable User getUser(@NotNull User user) {
        if (hasUser(user)) {
            return map.get(user.getPeerId()).get(user.getUserId());
        }
        return null;
    }
    @Override
    public @NotNull Optional<User> getUser(int peerId, int userId) {
        if (hasUser(peerId, userId)) {
            return Optional.of(map.get(peerId).get(userId));
        }
        return Optional.empty();
    }

    @Override
    public void deleteChat(int peerId) {
        map.remove(peerId);
    }

    @Override
    public User addIfAbsentAndReturn(int peerId, int userId) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        User user;
        if (users.containsKey(userId)) {
            user = users.get(userId);
        } else {
            user = new User(peerId, userId);
            if(VkUtils.isOwner(peerId, userId)) {
                user.setGroup(PermissionManager.getPermGroup(PermissionManager.ADMIN));
            }
            users.put(userId, user);
            this.saveAll();
        }
        return user;
    }
    private void addIfAbsentAndConsumer(@NotNull User entity, @NotNull Consumer<? super User> consumer) {
        Map<Integer, User> users = map.computeIfAbsent(entity.getPeerId(), k -> new HashMap<>());
        int userId = entity.getUserId();
        if(users.containsKey(userId)) {
            consumer.accept(users.get(userId));
        } else {
            consumer.accept(entity);
            users.put(userId, entity);
        }
        this.saveAll();
    }
    @Override
    public void setRank(int peerId, int userId, PermissionGroup rank) {
        addIfAbsentAndConsumer(new User(peerId, userId), user -> user.setGroup(rank));
    }
    @Override
    public void setRank(User user, PermissionGroup rank) {
        if(user == null) {
            return;
        }
        addIfAbsentAndConsumer(user, userId -> userId.setGroup(rank));
    }

    public void remove(@NotNull User user) {
        if (!map.containsKey(user.getPeerId())) {
            return;
        }
        Map<Integer, User> users = map.get(user.getPeerId());
        if(!users.containsKey(user.getUserId())) {
            return;
        }
        users.remove(user.getUserId());
        this.saveAll();
    }
    @Override
    public void deleteUser(int peerId, int userId) {
        if (!map.containsKey(peerId)) {
            return;
        }
        Map<Integer, User> users = map.get(peerId);
        if(!users.containsKey(userId)) {
            return;
        }
        users.remove(userId);
        this.saveAll();
    }
    @Override
    public void saveAll() {
        try (PrintWriter pw = new PrintWriter(getFile(), StandardCharsets.UTF_8)) {
            pw.print(getGson().toJson(map, new MapTypeToken().getType()));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
                users.put(id, new User(chat, id, group));
            }
            map.put(chat, users);
        }
        return map;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull Map<Integer, Map<Integer, User>> data, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for(Map.Entry<Integer, Map<Integer, User>> chat : data.entrySet()) {
            JsonObject peer = new JsonObject();
            for(Map.Entry<Integer, User> users : chat.getValue().entrySet()) {
                JsonObject user = new JsonObject();
                User account = users.getValue();
                user.addProperty("group", account.getGroup().getName());
                peer.add(String.valueOf(users.getKey()), user);
            }
            object.add(String.valueOf(chat.getKey()), peer);
        }
        return object;
    }

    private static class MapTypeToken extends TypeToken<Map<Integer, Map<Integer, User>>> {}
}
