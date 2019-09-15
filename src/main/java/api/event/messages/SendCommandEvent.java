package api.event.messages;

import api.entity.User;
import com.vk.api.sdk.objects.messages.Message;

public class SendCommandEvent extends MessageEvent {
    public SendCommandEvent(User user, Message msg) {
        super(user, msg);
    }
}
