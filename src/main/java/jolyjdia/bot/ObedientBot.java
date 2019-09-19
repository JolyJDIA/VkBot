package jolyjdia.bot;

import api.RoflanBot;
import api.scheduler.BotScheduler;
import api.utils.MathUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import org.jetbrains.annotations.Contract;

public final class ObedientBot implements RoflanBot {
    private final BotScheduler scheduler = new BotScheduler();

    @Contract(pure = true)
    @Override
    public BotScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void sendMessage(String msg, int peerId) {
        scheduler.runTask(() -> {
            try {
                send().peerId(peerId).message(msg).execute();
            } catch (ApiException | ClientException ignored) {}
        });
    }

    @Override
    public void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        scheduler.runTask(() -> {
            try {
                send().peerId(peerId).keyboard(keyboard).message(msg).execute();
            } catch (ApiException | ClientException ignored) {}
        });
    }

    @Override
    public void editChat(String title, int peerId) {
        scheduler.runTask(() -> {
            try {
                Loader.getVkApiClient().messages().editChat(Loader.getGroupActor(), peerId - 2000000000, title).execute();
            } catch (ApiException | ClientException ignored) { }
        });
    }

    private static MessagesSendQuery send() {
        return Loader.getVkApiClient().messages()
                .send(Loader.getGroupActor())
                .randomId(MathUtils.RANDOM.nextInt(10000))
                .groupId(Loader.GROUP_ID);
    }
}
