package api.utils;

import api.scheduler.BotScheduler;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.Random;

@NonNls public final class ObedientBot {
    public static final Random RANDOM = new Random();
    public static final BotScheduler SCHEDULER = new BotScheduler();

    @Contract(pure = true) private ObedientBot() {}

    public static void sendMessage(String msg, int peerId) {
        try {
            send().peerId(peerId).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    public static void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        try {
            send().peerId(peerId).keyboard(keyboard).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    private static MessagesSendQuery send() {
        return Bot.getVkApiClient().messages()
                .send(Bot.getGroupActor())
                .randomId(RANDOM.nextInt(10000))
                .groupId(Bot.GROUP_ID);
    }
    public static void editChat(String title, int peerId) {
        try {
            Bot.getVkApiClient().messages().editChat(Bot.getGroupActor(), peerId-2000000000, title).execute();
        } catch (ApiException | ClientException ignored) {}
    }
}
