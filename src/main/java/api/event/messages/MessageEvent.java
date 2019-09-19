package api.event.messages;

import api.entity.User;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class MessageEvent extends UserEvent {
    private final Message msg;

    @Contract(pure = true)
    public MessageEvent(User user, Message msg) {
        super(user);
        this.msg = msg;
    }

    @Contract(pure = true)
    public final Message getMessage() {
        return msg;
    }
}
