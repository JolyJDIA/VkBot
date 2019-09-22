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

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        ObedientBot bot = new ObedientBot();
        new Thread(
                new EventUpdater(new CallbackApiLongPollHandler(vkApiClient, bot.getGroupActor()))
        ).start();
        while (!Thread.currentThread().isInterrupted()) {
            bot.getScheduler().mainThreadHeartbeat();
            Thread.sleep(50);
        }
    }
    private static final class EventUpdater implements Runnable {
        private final CallbackApiLongPollHandler handler;

        @Contract(pure = true)
        private EventUpdater(CallbackApiLongPollHandler hander) {
            this.handler = hander;
        }
        @Override
        public void run() {
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
    }
    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }
}
