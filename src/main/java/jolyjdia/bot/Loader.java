package jolyjdia.bot;

import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.exceptions.LongPollServerKeyExpiredException;
import jolyjdia.vk.api.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import jolyjdia.vk.api.objects.groups.LongPollServer;
import org.jetbrains.annotations.Nullable;

public final class Loader {
    private Loader() {}

    static {
        Thread.currentThread().setName("Main Thread");
    }

    public static void main(String[] args) {
        LongPollServer longPollServer = getLongPollServer();
        if (longPollServer == null) {
            return;
        }
        int lastTimeStamp = Integer.parseInt(longPollServer.getTs());
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if(longPollServer == null) {
                    return;
                }
                GetLongPollEventsResponse eventsResponse = Bot.getVkApiClient()
                        .longPoll()
                        .getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp)
                        .waitTime(25)
                        .execute();
                if(eventsResponse == null) {
                    return;
                }
                eventsResponse.getUpdates().forEach(e -> Bot.getLongPoll().parse(e.getAsJsonObject()));
                lastTimeStamp = eventsResponse.getTs();
                Thread.sleep(50);
            } catch (LongPollServerKeyExpiredException | InterruptedException e) {
                longPollServer = getLongPollServer();
                e.printStackTrace();
            }
        }
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
