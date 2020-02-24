package jolyjdia.api.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jolyjdia.api.utils.chat.MessageChannel;
import jolyjdia.bot.Bot;
import jolyjdia.vk.api.client.actors.UserActor;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.objects.Validable;
import jolyjdia.vk.api.objects.audio.Audio;
import jolyjdia.vk.api.objects.photos.Photo;
import jolyjdia.vk.api.objects.video.Video;
import jolyjdia.vk.api.objects.wall.Wallpost;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class VkUtils {
    public static final UserActor ZAVR = new UserActor(310289867, Bot.getConfig().getProperty("ZavrAccessToken"));

    //СМЕНИТЬ
    public static final UserActor VALERA = new UserActor(526616439, Bot.getConfig().getProperty("ValeraAccessToken"));
    private VkUtils() {}

    @NonNls
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
    @NonNls
    public static @NotNull String attachment(@NotNull Validable body) {
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
        return "";
    }
    public static void sendPhoto(File photo, int peerId) {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpPost httppost = new HttpPost(String.valueOf(Bot.getVkApiClient().photos().getMessagesUploadServer(Bot.getGroupActor())
                    .peerId(peerId)
                    .execute()
                    .getUploadUrl()));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("photo",  new FileBody(photo))
                    .build();
            httppost.setEntity(reqEntity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);

            JsonElement jelement = new JsonParser().parse(responseBody);

            List<Photo> photos = Bot.getVkApiClient().photos()
                    .saveMessagesPhoto(Bot.getGroupActor(), jelement.getAsJsonObject().get("photo").getAsString())
                    .server(jelement.getAsJsonObject().get("server").getAsInt())
                    .hash(jelement.getAsJsonObject().get("hash").getAsString()).execute();
            MessageChannel.sendAttachments(photos.get(0), peerId);
        } catch (ClientException | ApiException | IOException e ) {
            e.printStackTrace();
        }
    }
}
