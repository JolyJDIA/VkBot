package jolyjdia.api.storage;

import jolyjdia.api.permission.PermissionGroup;
import jolyjdia.api.permission.PermissionManager;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 4943570635312868405L;
    private Chat<?> chat;
    @NonNls private transient int userId;
    private transient boolean change;
    private PermissionGroup group;
    private boolean owner;

    public User(int peerId, int userId) {
        this.userId = userId;
        this.chat = Bot.getUserBackend().getChatAndPutIfAbsent(peerId);
        this.group = PermissionManager.getDefault();
    }

    public User(int peerId, int userId, String group) {
        this(peerId, userId);
        this.group = PermissionManager.getPermGroup(group);
    }
    public User(int peerId, int userId, PermissionGroup group) {
        this(peerId, userId);
        this.group = group;
    }

    public final void setOwner(boolean owner) {
        this.owner = owner;
    }

    public final boolean isOwner() {
        return owner;
    }

    public final int getUserId() {
        return userId;
    }

    public final Chat<?> getChat() {
        return chat;
    }

    public final PermissionGroup getGroup() {
        return group;
    }

    public final void setGroup(PermissionGroup group) {
        this.group = group;
        this.change = true;
    }

    public final boolean isStaff() {
        return PermissionManager.isStaff(userId);
    }

    public final boolean unchanged() {
        return !change;
    }

    public final boolean isChange() {
        return change;
    }

    public final boolean hasPermission(String name) {
        return owner || isStaff() || group.hasPermission(name);
    }

    public final int getPeerId() {
        return chat.getPeerId();
    }

    @Override
    public final @NotNull String toString() {
        return "Айди-беседа: " + getPeerId() + '\n' +
                "Айди-пользователя: " + userId + '\n' +
                "Ранг: " + group.getName() + (owner ? "(OWNER)\n" : '\n') +
                "Префикс: " + group.getPrefix() + '\n' +
                "Суффикс: " + (group.getSuffix() == null && isStaff() ? "ЛОДОЧНИК" : group.getSuffix());
    }
    private void readObject(@NotNull java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.chat = (Chat<?>)stream.readObject();
        this.userId = stream.readInt();
        this.group = (PermissionGroup)stream.readObject();
        this.owner = stream.readBoolean();
        this.change = stream.readBoolean();
        stream.close();
    }
    private void writeObject(@NotNull java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.close();
    }
}
