package jolyjdia.bot;

import api.ProfileList;
import api.event.CallbackApiLongPollHandler;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.LongPollSettings;
import jolyjdia.bot.calculate.Respondent;
import jolyjdia.bot.translator.YandexTraslate;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Bot {
    public static final int GROUP_ID = 178836630;
    private static GroupActor groupActor;
    private static VkApiClient vkApiClient;
    private static ProfileList profileList;
    private static Properties properties;

    static {
        try (InputStream inputStream = Bot.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties = new Properties();
            if(inputStream != null) {
                properties.load(inputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws ClientException, ApiException {
        groupActor = new GroupActor(GROUP_ID, properties.getProperty("accessToken"));
        vkApiClient = new VkApiClient(new HttpTransportClient());
        LongPollSettings settings = vkApiClient.groups().getLongPollSettings(groupActor, GROUP_ID).execute();
        if (settings == null) {
            return;
        }

        if (!settings.getIsEnabled()) {
            vkApiClient.groups()
                    .setLongPollSettings(groupActor, GROUP_ID)
                    .enabled(true)
                    .wallPostNew(true)
                    .messageNew(true)
                    .execute();
        }
        profileList = new ProfileList(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, groupActor);
        registerAll();
        handler.run();
    }
    private static void registerAll() {
        new Respondent().onLoad();
        new YandexTraslate().onLoad();

    }

    @Contract(pure = true)
    public static ProfileList getProfileList() {
        return profileList;
    }

    @Contract(pure = true)
    public static GroupActor getGroupActor() {
        return groupActor;
    }

    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }

    @Contract(pure = true)
    public static Properties getConfig() {
        return properties;
    }
}
