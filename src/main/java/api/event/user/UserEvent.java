package api.event.user;

import api.event.Event;

public class UserEvent implements Event {
    private final int peerId;
    private final int userId;

    public UserEvent(int peerId, int userId) {
        this.peerId = peerId;
        this.userId = userId;
    }

    public final int getPeerId() {
        return peerId;
    }

    public final int getUserId() {
        return userId;
    }
}
