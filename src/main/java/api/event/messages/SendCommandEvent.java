package api.event.messages;

import com.vk.api.sdk.objects.messages.Message;

public class SendCommandEvent extends MessageEvent {

    public SendCommandEvent(Message msg) {
        super(msg);
    }
}
