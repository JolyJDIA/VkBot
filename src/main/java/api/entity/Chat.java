package api.entity;

import java.util.List;

/**
 * В ПИЗДУ
 */
public class Chat {
    private final int peerId;
    private String title;
    private final List<User> users;

    public Chat(int peerId, List<User> users) {
        this.peerId = peerId;
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public int getPeerId() {
        return peerId;
    }

    public String getTitle() {
        return title;
    }
}
