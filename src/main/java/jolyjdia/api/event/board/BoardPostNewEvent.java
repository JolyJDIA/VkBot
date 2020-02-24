package jolyjdia.api.event.board;

import jolyjdia.vk.api.objects.board.TopicComment;

public class BoardPostNewEvent extends BoardEvent {
    public BoardPostNewEvent(TopicComment topicComment) {
        super(topicComment);
    }
}
