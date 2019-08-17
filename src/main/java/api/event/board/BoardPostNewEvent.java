package api.event.board;

import com.vk.api.sdk.objects.board.TopicComment;

public class BoardPostNewEvent extends BoardEvent {
    public BoardPostNewEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
