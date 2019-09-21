package api.event.messages;

import api.storage.User;
import com.vk.api.sdk.objects.messages.Message;

public class EditMessageEvent extends MessageEvent {
    public EditMessageEvent(User user, Message message) {
        super(user, message);
    }
}
