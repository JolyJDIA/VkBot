package api.event.board;

import api.event.Event;
import com.vk.api.sdk.objects.board.TopicComment;

public class BoardEvent implements Event {
    private final TopicComment topicComment;

    public BoardEvent(TopicComment topicComment) {
        this.topicComment = topicComment;
    }

    public final TopicComment getTopicComment() {
        return topicComment;
    }
}
