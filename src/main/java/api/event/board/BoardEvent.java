package api.event.board;

import api.event.Event;
import com.vk.api.sdk.objects.board.TopicComment;
import org.jetbrains.annotations.Contract;

public class BoardEvent implements Event {
    private final TopicComment topicComment;
    @Contract(pure = true)
    public BoardEvent(TopicComment topicComment) {
        this.topicComment = topicComment;
    }

    @Contract(pure = true)
    public final TopicComment getTopicComment() {
        return topicComment;
    }
}
