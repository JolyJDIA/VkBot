package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;

public class ReplyMessageEvent extends MessageEvent {
    public ReplyMessageEvent(Message message) {
        super(message);
    }
}
