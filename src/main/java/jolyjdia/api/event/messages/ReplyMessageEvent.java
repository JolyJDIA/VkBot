package jolyjdia.api.event.messages;

import jolyjdia.api.storage.User;
import jolyjdia.vk.api.objects.messages.Message;

public class ReplyMessageEvent extends MessageEvent {
    public ReplyMessageEvent(User user, Message message) {
        super(user, message);
    }
}
