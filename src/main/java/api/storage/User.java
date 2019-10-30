package api.storage;

import api.Bot;
import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.Contract;

public class User  {
    @Expose(serialize = false, deserialize = false) private final int peerId;
    @Expose(serialize = false, deserialize = false) private final int userId;
    private PermissionGroup group;
    private boolean isOwner;

    @Contract(pure = true)
    //change place
    public User(int peerId, int userId) {
        this.peerId = peerId;
        this.userId = userId;
        this.group = PermissionManager.getDefault();
    }

    @Contract(pure = true)
    public User(int peerId, int userId, String group) {
        this.peerId = peerId;
        this.userId = userId;
        this.group = PermissionManager.getPermGroup(group);
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
    public final PermissionGroup getGroup() {
        return group;
    }

    public final void setGroup(PermissionGroup group) {
        this.group = group;
    }

    public final void sendMessageFromChat(String message, String... attachment) {
        Bot.sendMessage(message, peerId, attachment);
    }
}
