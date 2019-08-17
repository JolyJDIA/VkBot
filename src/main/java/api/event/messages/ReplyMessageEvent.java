package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class ReplyMessageEvent extends MessageEvent {
    @Contract(pure = true)
    public ReplyMessageEvent(Message message) {
        super(message);
    }
}
