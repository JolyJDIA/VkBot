package jolyjdia.api.storage;

import jolyjdia.api.utils.chat.MessageChannel;
import jolyjdia.bot.Bot;
import vk.exceptions.ApiException;
import vk.exceptions.ClientException;
import vk.objects.messages.Keyboard;

public class Chat<T> {
    private final int peerId;
    private final T container;

    protected Chat(T t, int peerId) {
        this.container = t;
        this.peerId = peerId;
    }

    public final int getPeerId() {
        return peerId;
    }
    public final void editChat(String title) {
        MessageChannel.editChat(title, peerId);
    }
    public final void sendMessage(String msg) {
        MessageChannel.sendMessage(msg, peerId);
    }
    public final void sendAttachments(String attachments) {
        MessageChannel.sendAttachments(attachments, peerId);
    }
    public final void sendMessage(String msg, String attachments) {
        MessageChannel.sendMessage(msg, peerId, attachments);
    }

    public final void sendKeyboard(String msg, Keyboard keyboard) {
        MessageChannel.sendKeyboard(msg, peerId, keyboard);
    }

    public final T getUsers() {
        return container;
    }
    public static boolean isOwner(int peerId, int userId) {
        try {
            return Bot.getVkApiClient().messages().getConversationMembers(Bot.getGroupActor(), peerId)
                    .execute().getItems()
                    .stream()
                    .anyMatch(e -> {
                        if (e.getMemberId() != userId) {
                            return false;
                        }
                        Boolean isOwner = e.getIsOwner();
                        Boolean isAdmin = e.getIsAdmin();
                        return (isOwner != null && isOwner) || (isAdmin != null && isAdmin);
                    });
        } catch (ApiException | ClientException e) {
            return false;
        }
    }
}
