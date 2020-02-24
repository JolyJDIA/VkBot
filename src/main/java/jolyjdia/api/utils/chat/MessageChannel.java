package jolyjdia.api.utils.chat;

import jolyjdia.api.utils.MathUtils;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.bot.Bot;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.objects.Validable;
import jolyjdia.vk.api.objects.messages.Keyboard;
import jolyjdia.vk.api.queries.messages.MessagesSendQuery;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

@NonNls
public final class MessageChannel {
    public static final int CHAT_ID = 2000000000;
    public static final int BOUND = 10000;

    private MessageChannel() {}

    public static void sendMessage(@NotNull String msg, int peerId) {
        try {
            if (msg.isEmpty()) {
                return;
            }
            msg = '[' +Thread.currentThread().getName()+"]: \n" + msg;
            builder(peerId).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    public static void sendAttachments(@NotNull String attachments, int peerId) {
        try {
            if (attachments.isEmpty()) {
                return;
            }
            builder(peerId).attachment(attachments).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    public static void sendAttachments(Validable body, int peerId) {
        try {
            String attachments = VkUtils.attachment(body);
            if (attachments.isEmpty()) {
                return;
            }
            builder(peerId).attachment(attachments).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void sendMessage(String msg, int peerId, String attachments) {
        try {
            msg = '[' +Thread.currentThread().getName()+"]: \n" + msg;
            builder(peerId).message(msg).attachment(attachments).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        try {
            msg = '[' +Thread.currentThread().getName()+"]: \n" + msg;
            builder(peerId).keyboard(keyboard).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void editChat(String title, int peerId) {
        try {
            Bot.getVkApiClient().messages().editChat(Bot.getGroupActor(), chatId(peerId), title).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static MessagesSendQuery builder(int peerId) {
        return Bot.getVkApiClient().messages()
                .send(Bot.getGroupActor())
                .randomId(MathUtils.RANDOM.nextInt(BOUND))
                .groupId(Bot.getGroupId())
                .peerId(peerId);
    }
    public static int chatId(int peerId) {
        return peerId-CHAT_ID;
    }
}
