package api.event.board;

import com.vk.api.sdk.objects.board.TopicComment;

public class BoardPostRestoreEvent extends BoardEvent {
    public BoardPostRestoreEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
