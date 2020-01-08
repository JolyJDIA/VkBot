package jolyjdia.api.event.board;

import vk.objects.board.TopicComment;

public class BoardPostEditEvent extends BoardEvent {
    public BoardPostEditEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
