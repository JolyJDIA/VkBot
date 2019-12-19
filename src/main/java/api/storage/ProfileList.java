package api.storage;

import api.file.JsonCustom;
import api.permission.PermissionManager;
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
        JsonDeserializer<Map<Integer, ChatCacheJSON>>,
        JsonSerializer<Map<Integer, ChatCacheJSON>> {
    private Map<Integer, ChatCacheJSON> chats = new HashMap<>();

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
            this.chats = this.getGson().fromJson(inputStreamReader, new MapTypeToken().getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull Set<Integer> getChats() {
        return chats.keySet();
    }

    private boolean hasUser(@NotNull User user) {
        return chats.containsKey(user.getChat().getPeerId()) && chats.get(user.getChat().getPeerId()).getUsers().containsKey(user.getUserId());
    }
    private boolean hasUser(int peerId, int userId) {
        return chats.containsKey(peerId) && chats.get(peerId).getUsers().containsKey(userId);
    }
    public @Nullable User getUser(@NotNull User user) {
        if (hasUser(user)) {
            return chats.get(user.getChat().getPeerId()).getUsers().get(user.getUserId());
        }
        return null;
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
    public User addIfAbsentAndReturn(int peerId, int userId) {
        ChatCacheJSON users = chats.computeIfAbsent(peerId, k -> new ChatCacheJSON(peerId));
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
    private void addIfAbsentAndConsumer(@NotNull User entity, @NotNull Consumer<? super User> consumer) {
        int peerId = entity.getChat().getPeerId();
        ChatCacheJSON users = chats.computeIfAbsent(peerId, k -> new ChatCacheJSON(peerId));
        int userId = entity.getUserId();
        if(users.getUsers().containsKey(userId)) {
            consumer.accept(users.getUsers().get(userId));
        } else {
            consumer.accept(entity);
            users.getUsers().put(userId, entity);
        }
        this.saveAll();
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
        Map<Integer, User> users = getChat(peerId).getUsers();
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
    public ChatCacheJSON getChat(int peerId) {
        return chats.computeIfAbsent(peerId, k -> new ChatCacheJSON(peerId));
    }

    @Override
    public Map<Integer, ChatCacheJSON> deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject obj = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> keyEntry : obj.entrySet()) {
            int chat = Integer.parseInt(keyEntry.getKey());
            ChatCacheJSON users = new ChatCacheJSON(chat);
            JsonObject object = keyEntry.getValue().getAsJsonObject();

            for (Map.Entry<String, JsonElement> valueEntry : object.entrySet()) {
                JsonObject element = valueEntry.getValue().getAsJsonObject();
                int id = Integer.parseInt(valueEntry.getKey());
                String group = element.get("group").getAsString();
                users.getUsers().put(id, new User(chat, id, group));
            }
            chats.put(chat, users);
        }
        return chats;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull Map<Integer, ChatCacheJSON> data, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for(Map.Entry<Integer, ChatCacheJSON> chat : data.entrySet()) {
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

    private static class MapTypeToken extends TypeToken<Map<Integer, ChatCacheJSON>> {}
}
