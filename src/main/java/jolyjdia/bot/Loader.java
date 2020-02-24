package jolyjdia.bot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jolyjdia.vk.api.callback.longpoll.CallbackApiLongPoll;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import jolyjdia.vk.api.objects.groups.LongPollServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;

public final class Loader {

    private Loader() {}

    static {
        Thread.currentThread().setName("Main Thread");
    }

    /**
     * @param args
     * @throws ClientException
     * @throws ApiException
     * @throws ClientException
     */
    public static void main(String[] args) {
        final CallbackApiLongPoll longPoll = new CallbackApiLongPollHandler(Bot.getVkApiClient(), Bot.getGroupActor());
        final LongPollServer longPollServer = Bot.getVkApiClient()
                .groupsLongPoll()
                .getLongPollServer(Bot.getGroupActor(), Bot.getGroupId())
                .execute();
        if(longPollServer == null) {
            return;
        }
        final CallbackHandler callabel = new CallbackHandler(longPollServer);
        AsyncResponse<GetLongPollEventsResponse> asyncResponse = new AsyncResponse<>(callabel);
        Future<GetLongPollEventsResponse> future = asyncResponse.submitAsync();
        while (!Thread.currentThread().isInterrupted()) {
            if(future.isDone()) {
                try {
                    GetLongPollEventsResponse response = future.get();
                    if(response != null) {
                        response.getUpdates().forEach(longPoll::parse);
                    }
                    future = asyncResponse.submitAsync();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            Bot.getScheduler().mainThreadHeartbeat();
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static class AsyncResponse<T> {
        private final ExecutorService executor = Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder()
                .setNameFormat("EventThread")
                .build()
        );
        private final Callable<T> callable;
        public AsyncResponse(Callable<T> callable) {
            this.callable = callable;
        }

        @NotNull
        public final Future<T> submitAsync() {
            return executor.submit(callable);
        }
    }

    public static class CallbackHandler implements Callable<GetLongPollEventsResponse> {
        private LongPollServer longPollServer;
        private volatile int lastTimeStamp;

        public CallbackHandler(@NotNull LongPollServer longPollServer) {
            this.longPollServer = longPollServer;
            this.lastTimeStamp = Integer.parseInt(longPollServer.getTs());
        }

        @Override
        public final GetLongPollEventsResponse call() {
            GetLongPollEventsResponse response = null;
            try {
                response = Bot.getVkApiClient()
                        .longPoll()
                        .getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp)
                        .waitTime(20)
                        .execute();
            } catch (ApiException | ClientException e) {
                longPollServer = getLongPollServer();
            }
            if(response != null) {
                this.lastTimeStamp = response.getTs();
            }
            return response;
        }

        @Nullable
        public static LongPollServer getLongPollServer() {
            try {
                return Bot.getVkApiClient()
                        .groupsLongPoll()
                        .getLongPollServer(Bot.getGroupActor(), Bot.getGroupId())
                        .execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
