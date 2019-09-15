package api.event.messages;

import api.entity.User;
import api.event.Event;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class MessageEvent implements Event {
    private final User user;
    private final Message msg;

    @Contract(pure = true)
    public MessageEvent(User user, Message msg) {
        this.user = user;
        this.msg = msg;
    }

    @Contract(pure = true)
    public final Message getMessage() {
        return msg;
    }

    @Contract(pure = true)
    public final User getUser() {
        return user;
    }
}
