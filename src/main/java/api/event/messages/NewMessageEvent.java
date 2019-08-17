package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class NewMessageEvent extends MessageEvent {
    @Contract(pure = true)
    public NewMessageEvent(Message message) {
        super(message);
    }
}
