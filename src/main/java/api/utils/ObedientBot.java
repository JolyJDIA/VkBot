package api.utils;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

@NonNls
public final class ObedientBot {
    @Contract(pure = true)
    private ObedientBot() {}
    public static void sendMessage(String msg, int peerId) {
        try {
            Bot.getVkApiClient().messages()
                    .send(Bot.getGroupActor())
                    .randomId(310289867)
                    .message(msg)
                    .groupId(Bot.GROUP_ID)
                    .peerId(peerId)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
    public static void editChat(String title, int peerId) {
        try {
            Bot.getVkApiClient().messages().editChat(Bot.getGroupActor(), peerId-2000000000, title).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
