package api.event.messages;

import api.event.Event;
import api.storage.Chat;
import api.storage.User;
import com.vk.api.sdk.objects.messages.Message;

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
