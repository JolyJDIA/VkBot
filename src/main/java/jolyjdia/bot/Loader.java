package jolyjdia.bot;

import api.Bot;
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

public class Loader {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());
    private static GroupActor groupActor;

    public static void main(String[] args) throws ClientException, ApiException {
        Bot.setBot(new ObedientBot());
        groupActor = new GroupActor(Bot.getGroupId(), Bot.getAccessToken());
        LongPollSettings settings = vkApiClient.groups().getLongPollSettings(groupActor, Bot.getGroupId()).execute();
        if (settings == null) {
            return;
        }
        if (!settings.getIsEnabled()) {
            vkApiClient.groups()
                    .setLongPollSettings(groupActor, Bot.getGroupId())
                    .enabled(true)
                    .wallPostNew(true)
                    .messageNew(true)
                    .audioNew(true)
                    .execute();
        }
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, groupActor);
        registerAll();
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
    }
    @Contract(pure = true)
    public static GroupActor getGroupActor() {
        return groupActor;
    }

    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }

}
