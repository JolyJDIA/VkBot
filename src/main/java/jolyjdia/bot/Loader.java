package jolyjdia.bot;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.objects.groups.LongPollServer;
import org.jetbrains.annotations.NotNull;

public final class Loader {
    //public static final Executor WORK_STEALING_POOL = Executors.newWorkStealingPool(1);

    private Loader() {}

    public static void main(String[] args) throws ClientException, ApiException {
        CallbackApiLongPoll longPoll = new CallbackApiLongPollHandler(Bot.getVkApiClient(), Bot.getGroupActor());
        LongPollServer pollServer = Bot.getVkApiClient()
                .groupsLongPoll()
                .getLongPollServer(Bot.getGroupActor(), Bot.getGroupId())
                .execute();
        final CallbackHandler handler = new CallbackHandler(longPoll, pollServer);
        while (!Thread.currentThread().isInterrupted()) {
            handler.run();
            Bot.getScheduler().mainThreadHeartbeat();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static class CallbackHandler implements Runnable {
        private final LongPollServer longPollServer;
        private final CallbackApiLongPoll handler;
        private int lastTimeStamp;

        public CallbackHandler(CallbackApiLongPoll handler, @NotNull LongPollServer longPollServer) {
            this.handler = handler;
            this.longPollServer = longPollServer;
            this.lastTimeStamp = Integer.parseInt(longPollServer.getTs());
        }

        @Override
        public final void run() {
            try {
                GetLongPollEventsResponse response = Bot.getVkApiClient()
                        .longPoll()
                        .getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp)
                        .execute();
                response.getUpdates().forEach(handler::parse);
                this.lastTimeStamp = response.getTs();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }
    }
}
