package api.entity;

import api.permission.PermissionGroup;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 3641738823927881630L;
    private transient int peerId;
    private transient int userId;
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

    private void readObject(@NotNull java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.peerId = stream.readInt();
        this.userId = stream.readInt();
        this.group = (String)stream.readObject();
        this.prefix = (String)stream.readObject();
        this.suffix = (String)stream.readObject();
        stream.close();
    }
    private void writeObject(@NotNull java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.close();
    }
}
