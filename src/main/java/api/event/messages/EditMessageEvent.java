package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;

public class EditMessageEvent extends MessageEvent {
    public EditMessageEvent(Message message) {
        super(message);
    }
}
