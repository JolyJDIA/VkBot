package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;

public class NewMessageEvent extends MessageEvent {
    public NewMessageEvent(Message message) {
        super(message);
    }
}
