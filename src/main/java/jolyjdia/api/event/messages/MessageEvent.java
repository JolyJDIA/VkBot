package jolyjdia.api.event.messages;

import jolyjdia.api.event.Event;
import jolyjdia.api.storage.Chat;
import jolyjdia.api.storage.User;
import vk.objects.messages.Message;

public class MessageEvent implements Event {
    private final Message msg;
    private final User user;

    public MessageEvent(User user, Message msg) {
        this.msg = msg;
        this.user = user;
    }

    public final User getUser() {
        return user;
    }

    public final Message getMessage() {
        return msg;
    }

    public final Chat<?> getChat() {
        return user.getChat();
    }
}
