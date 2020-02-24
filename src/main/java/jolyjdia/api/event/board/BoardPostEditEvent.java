package jolyjdia.api.event.board;

import jolyjdia.vk.api.objects.board.TopicComment;

public class BoardPostEditEvent extends BoardEvent {
    public BoardPostEditEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
