package api.utils.chat;

import api.utils.MathUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MessageChannel {
    @Contract(pure = true)
    private MessageChannel() {}

    public static void sendMessage(@NotNull String msg, int peerId) {
        try {
            MessagesSendQuery query = builder(peerId);
            if (!msg.isEmpty()) {
                query.message(msg);
            }
            query.execute();
        } catch (ApiException | ClientException ignored) {}
    }
    public static void sendAttachments(@NotNull String attachments, int peerId) {
        try {
            MessagesSendQuery query = builder(peerId);
            if (!attachments.isEmpty()) {
                query.attachment(attachments);
            }
            query.execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void sendMessage(String msg, int peerId, @NotNull String attachments) {
        try {
            MessagesSendQuery query = builder(peerId);
            if (msg != null && !msg.isEmpty()) {
                query.message(msg);
            }
            if (!attachments.isEmpty()) {
                query.attachment(attachments);
            }
            query.execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        try {
            builder(peerId).keyboard(keyboard).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void editChat(String title, int peerId) {
        try {
            Bot.getVkApiClient().messages().editChat(Bot.getGroupActor(), peerId-2000000000, title).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static MessagesSendQuery builder(int peerId) {
        return Bot.getVkApiClient().messages()
                .send(Bot.getGroupActor())
                .randomId(MathUtils.RANDOM.nextInt(10000))
                .groupId(Bot.getGroupId())
                .peerId(peerId);
    }
}
