package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;

public class EditMessageEvent extends MessageEvent {
    @Contract(pure = true)
    public EditMessageEvent(Message message) {
        super(message);
    }
}
