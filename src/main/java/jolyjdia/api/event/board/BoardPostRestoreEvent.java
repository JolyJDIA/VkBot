package jolyjdia.api.event.board;

import vk.objects.board.TopicComment;

public class BoardPostRestoreEvent extends BoardEvent {
    public BoardPostRestoreEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
