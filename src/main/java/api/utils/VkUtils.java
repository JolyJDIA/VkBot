package api.utils;

import api.storage.User;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.Validable;
import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.messages.responses.GetConversationMembersResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.Wallpost;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VkUtils {
    public static final UserActor USER_ACTOR = new UserActor(310289867, Bot.getConfig().getProperty("UserAccessToken"));
    @Contract(pure = true)
    private VkUtils() {}

    @NonNls
    @Contract(pure = true)
    public static @NotNull String attachment(String body, int ownerId, int id) {
        return body + ownerId + '_' + id;
    }

    public static @Nullable Integer getUserId(@NotNull String s, @NotNull User sender) {
        try {
            int id = s.charAt(0) == '[' ? getIdNick(s) : getIdString(s);
            GetConversationMembersResponse g = Bot.getVkApiClient()
                    .messages()
                    .getConversationMembers(Bot.getGroupActor(), sender.getPeerId())
                    .execute();
            return id;
        } catch (ApiException | ClientException e) {
            sender.sendMessageFromChat("Пользователя нет в беседе");
        }
        return null;
    }
    public static int getIdNick(@NotNull String a) {
        return Integer.parseInt(a.substring(3).split("\\|")[0]);
    }

    public static int getIdString(String a) {
        return Integer.parseInt(a);
    }

    @Contract(pure = true)
    @NonNls
    public static @Nullable String attachment(Object body) {
        if(!Validable.class.isAssignableFrom(body.getClass())) {
            return null;
        }
        if(Wallpost.class.isAssignableFrom(body.getClass())) {
            Wallpost wallpost = (Wallpost)body;
            return "wall"+wallpost.getOwnerId()+ '_' +wallpost.getId();
        } else if(Photo.class.isAssignableFrom(body.getClass())) {
            Photo photo = (Photo)body;
            return "photo"+photo.getOwnerId()+ '_' +photo.getId();
        } else if(Audio.class.isAssignableFrom(body.getClass())) {
            Audio audio = (Audio)body;
            return "audio"+audio.getId()+ '_' +audio.getId();
        } else if(Video.class.isAssignableFrom(body.getClass())) {
            Video video = (Video)body;
            return "video"+video.getOwnerId()+ '_' +video.getId();
        }
        return null;
    }
}
