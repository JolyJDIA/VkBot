package jolyjdia.api.event.post;

import vk.objects.wall.Wallpost;

public class RepostWallEvent extends WallpostEvent {
    public RepostWallEvent(Wallpost wallpost) {
        super(wallpost);
    }
}
