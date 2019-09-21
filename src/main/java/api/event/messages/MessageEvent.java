package api.event.messages;

import api.event.Event;
import api.storage.User;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class MessageEvent implements Event {
    private final Message msg;
    private final User user;

    @Contract(pure = true)
    public MessageEvent(User user, Message msg) {
        this.msg = msg;
        this.user = user;
    }

    @Contract(pure = true)
    public final User getUser() {
        return user;
    }

    @Contract(pure = true)
    public final Message getMessage() {
        return msg;
    }
}
