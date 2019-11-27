package api.storage;

import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import api.utils.chat.MessageChannel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 4943570635312868405L;
    @NonNls private transient int peerId;
    private transient int userId;
    private PermissionGroup group;
    private boolean owner;

    @Contract(pure = true)
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
    public User(int peerId, int userId, PermissionGroup group) {
        this.peerId = peerId;
        this.userId = userId;
        this.group = group;
    }

    public final void setOwner(boolean owner) {
        this.owner = owner;
    }
    @Contract(pure = true)
    public final boolean isOwner() {
        return owner;
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

    public final void sendMessage(String message, String attachment) {
        MessageChannel.sendMessage(message, peerId, attachment);
    }
    public final void sendMessage(String message) {
        MessageChannel.sendMessage(message, peerId);
    }

    @Override
    public final @NotNull String toString() {
        return "Айди-беседа: " + peerId + '\n' +
                "Айди-пользователя: " + userId + '\n' +
                "Ранг: " + group.getName() + (owner ? "(OWNER)\n" : '\n') +
                "Префикс: " + group.getPrefix() + '\n' +
                "Суффикс: " + (group.getSuffix() == null && PermissionManager.isStaff(userId) ? "ЛОДОЧНИК" : group.getSuffix());
    }
    private void readObject(@NotNull java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.peerId = stream.readInt();
        this.userId = stream.readInt();
        this.group = (PermissionGroup)stream.readObject();
        this.owner = stream.readBoolean();
        stream.close();
    }
    private void writeObject(@NotNull java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.close();
    }
}
