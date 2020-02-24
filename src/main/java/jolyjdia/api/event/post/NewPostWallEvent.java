package jolyjdia.api.event.post;

import jolyjdia.vk.api.objects.wall.Wallpost;

public class NewPostWallEvent extends WallpostEvent {
    public NewPostWallEvent(Wallpost wallpost) {
        super(wallpost);
    }
}
