package jolyjdia.bot;

import api.Bot;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.objects.groups.LongPollServer;
import org.jetbrains.annotations.Contract;

public class Loader {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());

    public static void main(String[] args) throws ClientException, ApiException {
        ObedientBot bot = new ObedientBot();
        new Thread(new EventUpdater()).start();

        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, bot.getGroupActor());
        try {
            LongPollServer longPollServer = getLongPollServer();
            int lastTimeStamp = Integer.parseInt(longPollServer.getTs());
            while (!Thread.currentThread().isInterrupted()) {
                GetLongPollEventsResponse eventsResponse = vkApiClient
                        .longPoll()
                        .getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp)
                        .waitTime(25)
                        .execute();
                for (JsonObject jsonObject : eventsResponse.getUpdates()) {
                    handler.parse(jsonObject);
                }
                lastTimeStamp = eventsResponse.getTs();
                Thread.sleep(50);
            }
        } catch (ClientException | ApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static LongPollServer getLongPollServer() throws ClientException, ApiException {
        return vkApiClient.groupsLongPoll().getLongPollServer(Bot.getGroupActor(), Bot.getGroupId()).execute();
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
