package api.event.messages;

import api.storage.User;
import com.vk.api.sdk.objects.messages.Message;

public class ReplyMessageEvent extends MessageEvent {
    public ReplyMessageEvent(User user, Message message) {
        super(user, message);
    }
}
