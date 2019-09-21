package api.storage;

import api.Bot;
import api.permission.PermissionGroup;
import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.Contract;

public class User  {
    @Expose(serialize = false, deserialize = false) private final int peerId;
    @Expose(serialize = false, deserialize = false) private final int userId;
    private String group = PermissionGroup.DEFAULT;
    private String prefix = "";
    private String suffix = "";

    @Contract(pure = true)
    public User(int peerId, int userId) {
        this.peerId = peerId;
        this.userId = userId;
    }
    @Contract(pure = true)
    public User(int peerId, int userId, String group) {
        this(peerId, userId);
        this.group = group;
    }

    @Contract(pure = true)
    public User(int peerId, int userId, String group, String prefix) {
        this(peerId, userId, group);
        this.prefix = prefix;
    }

    @Contract(pure = true)
    public User(int peerId, int userId, String group, String prefix, String suffix) {
        this(peerId, userId, group, prefix);
        this.suffix = suffix;
    }
    @Contract(pure = true)
    public final int getUserId() {
        return userId;
    }

    @Contract(pure = true)
    public final int getPeerId() {
        return peerId;
    }

    @Contract(pure = true)
    public final String getGroup() {
        return group;
    }

    @Contract(pure = true)
    public final String getSuffix() {
        return suffix;
    }

    @Contract(pure = true)
    public final String getPrefix() {
        return prefix;
    }

    public final void setGroup(String group) {
        this.group = group;
    }
    public final void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public final void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public final void sendMessageFromHisChat(String message) {
        Bot.sendMessage(message, peerId);
    }
}
