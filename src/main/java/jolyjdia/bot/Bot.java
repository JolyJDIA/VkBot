package jolyjdia.bot;

import api.CallbackApiLongPollHandler;
import api.ProfileList;
import api.utils.Watchdog;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.LongPollSettings;
import jolyjdia.bot.calculate.CalculatorRegister;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.kubanoid.KubanoidLoad;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.shkila.Timetable;
import jolyjdia.bot.translator.YandexTraslate;
import org.jetbrains.annotations.Contract;

import java.io.File;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static final String ACCESS_TOKEN = properties.getProperty("accessToken");
    public static void main(String[] args) throws ClientException, ApiException {
        groupActor = new GroupActor(GROUP_ID, ACCESS_TOKEN);
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
        Watchdog.doStart();
        profileList = new ProfileList(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, groupActor);
        registerAll();
        handler.run();
    }
    private static void registerAll() {
        new CalculatorRegister().onLoad();
        new YandexTraslate().onLoad();
        new GeoLoad().onLoad();
        new Puzzle().onLoad();
        //new ShoutboxMain().onLoad();
        new KubanoidLoad().onLoad();
        new Timetable().onLoad();
       // new GeneratorLoad().onLoad();
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
