package jolyjdia.api.event.post;

import jolyjdia.api.event.Event;
import jolyjdia.vk.api.objects.wall.Wallpost;

public class WallpostEvent implements Event {
    private final Wallpost wallpost;

    public WallpostEvent(Wallpost wallpost) {
        this.wallpost = wallpost;
    }

    public final Wallpost getWallpost() {
        return wallpost;
    }
}
