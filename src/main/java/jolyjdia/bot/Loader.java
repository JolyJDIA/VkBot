package jolyjdia.bot;

import api.Bot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.jetbrains.annotations.Contract;

public class Loader {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());

    public static void main(String[] args) throws ClientException, ApiException {
        ObedientBot bot = new ObedientBot();
        new Thread(new EventUpdater()).start();

        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, bot.getGroupActor());
        handler.run();
    }
    private static final class EventUpdater implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Bot.getScheduler().mainThreadHeartbeat();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }
}
