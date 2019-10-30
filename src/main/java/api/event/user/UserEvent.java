package api.event.user;

import api.event.Event;
import org.jetbrains.annotations.Contract;

public class UserEvent implements Event {
    private final int peerId;
    private final int userId;

    @Contract(pure = true)
    public UserEvent(int peerId, int userId) {
        this.peerId = peerId;
        this.userId = userId;
    }

    @Contract(pure = true)
    public final int getPeerId() {
        return peerId;
    }

    @Contract(pure = true)
    public final int getUserId() {
        return userId;
    }
}
