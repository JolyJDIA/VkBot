package jolyjdia.api.storage;

import com.google.gson.annotations.Expose;
import jolyjdia.api.permission.PermissionGroup;
import jolyjdia.api.permission.PermissionManager;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class User {
    private final Chat<?> chat;

    @Expose(serialize = false, deserialize = false)
    @NonNls private final int userId;

    @Expose(serialize = false, deserialize = false)
    private boolean change;

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
}
