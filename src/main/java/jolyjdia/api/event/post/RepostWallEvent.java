package jolyjdia.api.event.post;

import jolyjdia.vk.api.objects.wall.Wallpost;

public class RepostWallEvent extends WallpostEvent {
    public RepostWallEvent(Wallpost wallpost) {
        super(wallpost);
    }
}
