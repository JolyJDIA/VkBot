package api;

import api.entity.User;
import api.file.FileCustom;
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
import java.util.Set;
import java.util.function.Consumer;

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
    @Nullable
    private User getUser(@NotNull User user) {
        if (hasUser(user)) {
            return map.get(user.getPeerId()).get(user.getUserId());
        }
        return null;
    }

    @Nullable
    private User getUser(int peerId, int userId) {
        if (hasUser(peerId, userId)) {
            return map.get(peerId).get(userId);
        }
        return null;
    }

    public User addIfAbsentAndReturn(int peerId, int userId) {
        Map<Integer, User> users = map.computeIfAbsent(peerId, k -> new HashMap<>());
        User user;
        if (users.containsKey(userId)) {
            user = users.get(userId);
        } else {
            user = new User(peerId, userId);
            users.put(userId, user);
            this.save();
        }
        return user;
    }
    public void addIfAbsentAndConsumer(@NotNull User entity, @NotNull Consumer<? super User> consumer) {
        Map<Integer, User> users = map.computeIfAbsent(entity.getPeerId(), k -> new HashMap<>());
        int userId = entity.getUserId();
        if(users.containsKey(userId)) {
            consumer.accept(users.get(userId));
        } else {
            consumer.accept(entity);
            users.put(userId, entity);
        }
        this.save();
    }
    public void setRank(int peerId, int userId, String rank) {
        addIfAbsentAndConsumer(new User(peerId, userId), userId1 -> userId1.setGroup(rank));
    }
    public void setPrefix(int peerId, int userId, String prefix) {
        addIfAbsentAndConsumer(new User(peerId, userId), userId1 -> userId1.setPrefix(prefix));
    }
    public void setSuffix(int peerId, int userId, String suffix) {
        addIfAbsentAndConsumer(new User(peerId, userId), userId1 -> userId1.setSuffix(suffix));
    }
    public void setRank(User user, String rank) {
        if(user == null) {
            return;
        }
        addIfAbsentAndConsumer(user, userId -> userId.setGroup(rank));
    }
    public void setPrefix(User user, String prefix) {
        if(user == null) {
            return;
        }
        addIfAbsentAndConsumer(user, userId -> userId.setPrefix(prefix));
    }

    public void setSuffix(User user, String suffix) {
        if(user == null) {
            return;
        }
        addIfAbsentAndConsumer(user, userId -> userId.setPrefix(suffix));
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
