package api.event.post;

import com.vk.api.sdk.objects.wall.Wallpost;

public class NewPostWallEvent extends WallpostEvent {
    public NewPostWallEvent(Wallpost wallpost) {
        super(wallpost);
    }
}
