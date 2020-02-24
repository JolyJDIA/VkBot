package jolyjdia.api.event.board;

import jolyjdia.vk.api.objects.board.TopicComment;

public class BoardPostRestoreEvent extends BoardEvent {
    public BoardPostRestoreEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
