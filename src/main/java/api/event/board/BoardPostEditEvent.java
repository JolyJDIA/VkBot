package api.event.board;

import com.vk.api.sdk.objects.board.TopicComment;

public class BoardPostEditEvent extends BoardEvent {
    public BoardPostEditEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
