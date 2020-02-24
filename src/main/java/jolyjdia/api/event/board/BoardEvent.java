package jolyjdia.api.event.board;

import jolyjdia.api.event.Event;
import jolyjdia.vk.api.objects.board.TopicComment;

public class BoardEvent implements Event {
    private final TopicComment topicComment;

    public BoardEvent(TopicComment topicComment) {
        this.topicComment = topicComment;
    }

    public final TopicComment getTopicComment() {
        return topicComment;
    }
}
