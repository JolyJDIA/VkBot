package api.event.post;

import api.event.Event;
import com.vk.api.sdk.objects.wall.Wallpost;
import org.jetbrains.annotations.Contract;

public class WallpostEvent implements Event {
    private final Wallpost wallpost;
    @Contract(pure = true)
    public WallpostEvent(Wallpost wallpost) {
        this.wallpost = wallpost;
    }

    @Contract(pure = true)
    public final Wallpost getWallpost() {
        return wallpost;
    }
}
