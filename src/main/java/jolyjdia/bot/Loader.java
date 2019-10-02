package jolyjdia.bot;

import api.RoflanBot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
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
            System.out.println("ПРОИЗОШЕЛ СБОЙ, ЗАПУСКАЮСЬ ЗАНОВО");
            handler.run();
        }
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
