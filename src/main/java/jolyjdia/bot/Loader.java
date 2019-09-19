package jolyjdia.bot;

import api.Bot;
import api.Watchdog;
import api.file.ProfileList;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.LongPollSettings;
import jolyjdia.bot.calculate.CalculatorRegister;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.kubanoid.KubanoidLoad;
import jolyjdia.bot.password.GeneratorPassword;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.shoutbox.ShoutboxMain;
import jolyjdia.bot.translator.YandexTraslate;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Loader {
    private static final Properties properties = new Properties();
    public static final int GROUP_ID = 178836630;
    private static final ProfileList profileList;

    static {
        try (InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if(inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileList = new ProfileList(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));
    }
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());
    public static final String ACCESS_TOKEN = properties.getProperty("accessToken");
    private static final GroupActor groupActor = new GroupActor(GROUP_ID, ACCESS_TOKEN);

    public static void main(String[] args) throws ClientException, ApiException {
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
                    .audioNew(true)
                    .execute();
        }
        Bot.setBot(new ObedientBot());
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, groupActor);
        registerAll();
        Watchdog.doStart();
        handler.run();
    }
    private static void registerAll() {
        new CalculatorRegister().onLoad();
        new YandexTraslate().onLoad();
        new GeoLoad().onLoad();
        new Puzzle().onLoad();
        new ShoutboxMain().onLoad();
        new KubanoidLoad().onLoad();
        new GeneratorPassword().onLoad();
        //new Raid().onLoad();
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
