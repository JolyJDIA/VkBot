package jolyjdia.bot;

import api.Bot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import jolyjdia.bot.calculate.CalculatorRegister;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.kubanoid.KubanoidLoad;
import jolyjdia.bot.password.GeneratorPassword;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.shoutbox.ShoutboxMain;
import jolyjdia.bot.translator.YandexTraslate;
import org.jetbrains.annotations.Contract;

import java.util.Timer;
import java.util.TimerTask;

public class Loader {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());

    public static void main(String[] args) throws ClientException, ApiException {
        Bot.setBot(new ObedientBot());
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, Bot.getGroupActor());
        registerModules();
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Bot.getScheduler().mainThreadHeartbeat();
            }
        }, 0, 50);
        handler.run();
    }
    private static void registerModules() {
        new CalculatorRegister().onLoad();
        new YandexTraslate().onLoad();
        new GeoLoad().onLoad();
        new Puzzle().onLoad();
        new ShoutboxMain().onLoad();
        new KubanoidLoad().onLoad();
        new GeneratorPassword().onLoad();
    }

    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }

}
