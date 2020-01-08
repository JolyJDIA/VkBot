package jolyjdia.bot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vk.callback.longpoll.CallbackApiLongPoll;
import vk.exceptions.ApiException;
import vk.exceptions.ClientException;
import vk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import vk.objects.groups.LongPollServer;

import java.util.concurrent.*;

public final class Loader {

    private Loader() {}

    static {
        Thread.currentThread().setName("Main Thread");
    }

    public static void main(String[] args) throws ClientException, ApiException {
        final CallbackApiLongPoll longPoll = new CallbackApiLongPollHandler(Bot.getVkApiClient(), Bot.getGroupActor());
        final LongPollServer longPollServer = Bot.getVkApiClient()
                .groupsLongPoll()
                .getLongPollServer(Bot.getGroupActor(), Bot.getGroupId())
                .execute();
        final CallbackHandler callabel = new CallbackHandler(longPollServer);
        AsyncResponse<GetLongPollEventsResponse> asyncResponse = new AsyncResponse<>(callabel);
        Future<GetLongPollEventsResponse> future = asyncResponse.get();
        while (!Thread.currentThread().isInterrupted()) {
            if(future.isDone()) {
                try {
                    GetLongPollEventsResponse response = future.get();
                    if(response != null) {
                        response.getUpdates().forEach(longPoll::parse);
                    }
                    future = asyncResponse.get();
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
        public final Future<T> get() {
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
