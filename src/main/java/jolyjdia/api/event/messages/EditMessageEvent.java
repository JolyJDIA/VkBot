package jolyjdia.api.event.messages;

import jolyjdia.api.storage.User;
import jolyjdia.vk.api.objects.messages.Message;

public class EditMessageEvent extends MessageEvent {
    public EditMessageEvent(User user, Message message) {
        super(user, message);
    }
}
