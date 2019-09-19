package api.event.messages;

import api.entity.User;
import api.event.Event;
import org.jetbrains.annotations.Contract;

public class UserEvent implements Event {
    private final User user;

    @Contract(pure = true)
    public UserEvent(User user) {
        this.user = user;
    }

    @Contract(pure = true)
    public final User getUser() {
        return user;
    }
}