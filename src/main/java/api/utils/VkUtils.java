package api.utils;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.Validable;
import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.Wallpost;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class VkUtils {
    public static final UserActor USER_ACTOR = new UserActor(310289867, Bot.getConfig().getProperty("UserAccessToken"));

    @Contract(pure = true)
    private VkUtils() {}

    @NonNls
    @Contract(pure = true)
    public static @NotNull String attachment(String body, int ownerId, int id) {
        return body + ownerId + '_' + id;
    }

    //[idasdasda|]
    public static Optional<Integer> getUserId(@NotNull String s) {
        try {
            return Optional.of(s.charAt(0) == '[' ? Integer.parseInt(s.substring(3).split("\\|")[0]) : Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    @Contract(pure = true)
    @NonNls
    public static Optional<String> attachment(@NotNull Object body) {
        if(!Validable.class.isAssignableFrom(body.getClass())) {
            return Optional.empty();
        }
        if(Wallpost.class.isAssignableFrom(body.getClass())) {
            Wallpost wallpost = (Wallpost)body;
            return Optional.of("wall"+wallpost.getOwnerId()+ '_' +wallpost.getId());
        } else if(Photo.class.isAssignableFrom(body.getClass())) {
            Photo photo = (Photo)body;
            return Optional.of("photo"+photo.getOwnerId()+ '_' +photo.getId());
        } else if(Audio.class.isAssignableFrom(body.getClass())) {
            Audio audio = (Audio)body;
            return Optional.of("audio"+audio.getId()+ '_' +audio.getId());
        } else if(Video.class.isAssignableFrom(body.getClass())) {
            Video video = (Video)body;
            return Optional.of("video"+video.getOwnerId()+ '_' +video.getId());
        }
        return Optional.empty();
    }
}
