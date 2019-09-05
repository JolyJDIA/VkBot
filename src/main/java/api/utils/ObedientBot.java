package api.utils;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.Random;

@NonNls
public final class ObedientBot {

    @Contract(pure = true)
    private ObedientBot() {}
    public static void sendMessage(String msg, int peerId) {
        try { Bot.getVkApiClient().messages()
                .send(Bot.getGroupActor())
                .randomId(new Random().nextInt(10000))
                .message(msg)
                .groupId(Bot.GROUP_ID)
                .peerId(peerId)
                .execute();
        } catch (ApiException | ClientException ignored) {}
    }
    public static void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        try { Bot.getVkApiClient().messages()
                .send(Bot.getGroupActor())
                .randomId(new Random().nextInt(10000))
                .message(msg)
                .groupId(Bot.GROUP_ID)
                .peerId(peerId)
                .keyboard(keyboard)
                .execute();
        } catch (ApiException | ClientException ignored) {}
    }
    public static void editChat(String title, int peerId) {
        try {
            Bot.getVkApiClient().messages().editChat(Bot.getGroupActor(), peerId-2000000000, title).execute();
        } catch (ApiException | ClientException ignored) {}
    }
}
