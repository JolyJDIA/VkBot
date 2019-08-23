package api.event.messages;

import api.event.Event;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class MessageEvent implements Event {
    private final Message msg;

    @Contract(pure = true)
    public MessageEvent(Message msg) {
        this.msg = msg;
    }

    @Contract(pure = true)
    public final Message getMessage() {
        return msg;
    }
}
