package jolyjdia.api.event.post;

import vk.objects.wall.Wallpost;

public class NewPostWallEvent extends WallpostEvent {
    public NewPostWallEvent(Wallpost wallpost) {
        super(wallpost);
    }
}
