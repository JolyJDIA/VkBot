package api.event.post;

import api.event.Event;
import com.vk.api.sdk.objects.wall.Wallpost;

public class WallpostEvent implements Event {
    private final Wallpost wallpost;

    public WallpostEvent(Wallpost wallpost) {
        this.wallpost = wallpost;
    }

    public final Wallpost getWallpost() {
        return wallpost;
    }
}
