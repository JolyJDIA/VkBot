package jolyjdia.bot;

import api.Bot;
import api.RoflanBot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.LongPollServer;
import org.jetbrains.annotations.Contract;

public class Loader {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());

    public static void main(String[] args) throws ClientException, ApiException {
        ObedientBot bot = new ObedientBot();
        new Thread(new EventUpdater(bot)).start();

        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, bot.getGroupActor());

        try {
            handler.run();
        } catch (ApiException | ClientException | RuntimeException e) {
            System.out.println("ТЕХНИЧЕСКИЕ ШОКОЛАДКИ");
            handler = new CallbackApiLongPollHandler(vkApiClient, bot.getGroupActor());
            handler.run();
        }
    }
    private static LongPollServer getLongPollServer() throws ClientException, ApiException {
        return vkApiClient.groupsLongPoll().getLongPollServer(Bot.getGroupActor(), Bot.getGroupId()).execute();
    }
    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }
    private static final class EventUpdater implements Runnable {
        private final RoflanBot bot;
        @Contract(pure = true)
        private EventUpdater(RoflanBot bot) {
            this.bot = bot;
        }
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                bot.getScheduler().mainThreadHeartbeat();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
