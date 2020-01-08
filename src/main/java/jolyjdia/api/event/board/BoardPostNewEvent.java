package jolyjdia.api.event.board;

import vk.objects.board.TopicComment;

public class BoardPostNewEvent extends BoardEvent {
    public BoardPostNewEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
