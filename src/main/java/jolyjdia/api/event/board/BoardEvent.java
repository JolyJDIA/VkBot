package jolyjdia.api.event.board;

import jolyjdia.api.event.Event;
import vk.objects.board.TopicComment;

public class BoardEvent implements Event {
    private final TopicComment topicComment;

    public BoardEvent(TopicComment topicComment) {
        this.topicComment = topicComment;
    }

    public final TopicComment getTopicComment() {
        return topicComment;
    }
}
