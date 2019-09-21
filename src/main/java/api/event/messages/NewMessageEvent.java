package api.event.messages;

import api.storage.User;
import com.vk.api.sdk.objects.messages.Message;

public class NewMessageEvent extends MessageEvent {
    public NewMessageEvent(User user, Message message) {
        super(user, message);
    }
}
