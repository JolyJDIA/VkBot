package jolyjdia.api.event.messages;

import jolyjdia.api.storage.User;
import vk.objects.messages.Message;

public class NewMessageEvent extends MessageEvent {
    public NewMessageEvent(User user, Message message) {
        super(user, message);
    }
}
