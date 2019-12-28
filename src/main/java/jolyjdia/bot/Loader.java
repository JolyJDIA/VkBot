package jolyjdia.bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.objects.groups.LongPollServer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class Loader {
    public static final Executor WORK_STEALING_POOL = Executors.newWorkStealingPool(1);

    private Loader() {}

    public static void main(String[] args) throws ClientException, ApiException {
        final CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(Bot.getVkApiClient(), Bot.getGroupActor());
        final LongPollServer longPollServer = Bot.getVkApiClient()
                .groupsLongPoll()
                .getLongPollServer(Bot.getGroupActor(), Bot.getGroupId())
                .execute();
        AtomicInteger lastTimeStamp = new AtomicInteger(Integer.parseInt(longPollServer.getTs()));
        final Runnable runnable = () -> {
            try {
                GetLongPollEventsResponse f = Bot.getVkApiClient()
                        .longPoll()
                        .getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp.get())
                        .waitTime(25)
                        .execute();
                f.getUpdates().forEach(handler::parse);
                lastTimeStamp.set(f.getTs());
            } catch (ApiException | ClientException  e) {
                e.printStackTrace();
            }
        };
        while (!Thread.currentThread().isInterrupted()) {
            WORK_STEALING_POOL.execute(runnable);
            Bot.getScheduler().mainThreadHeartbeat();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
