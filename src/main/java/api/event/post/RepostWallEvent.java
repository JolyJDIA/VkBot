package api.event.post;

import com.vk.api.sdk.objects.wall.Wallpost;

public class RepostWallEvent extends WallpostEvent {
    public RepostWallEvent(Wallpost wallpost) {
        super(wallpost);
    }
}
