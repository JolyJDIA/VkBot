package jolyjdia.bot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.jetbrains.annotations.Contract;

public class Loader {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());

    public static void main(String[] args) throws ClientException, ApiException {
        new Thread(new EventUpdater()).start();
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, Bot.getGroupActor());

        try {
            handler.run();
        } catch (ApiException | ClientException | RuntimeException e) {
            System.out.println("ТЕХНИЧЕСКИЕ ШОКОЛАДКИ");
            handler = new CallbackApiLongPollHandler(vkApiClient, Bot.getGroupActor());
            handler.run();
        }
    }
    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }
    private static final class EventUpdater implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Bot.getScheduler().mainThreadHeartbeat();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
