package api.storage;

import api.utils.chat.MessageChannel;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;


public class Chat<T> {//implements UserBackend {
    private final int peerId;
    private final T container;

    protected Chat(T t, int peerId) {
        this.container = t;
        this.peerId = peerId;
    }

    public final int getPeerId() {
        return peerId;
    }

    public static boolean isOwner(int peerId, int userId) {
        try {
            return Bot.getVkApiClient().messages().getConversationMembers(Bot.getGroupActor(), peerId).execute().getItems()
                    .stream()
                    .anyMatch(e -> {
                        if(e.getMemberId() != userId) {
                            return false;
                        }
                        Boolean isOwner = e.getIsOwner();
                        Boolean isAdmin = e.getIsAdmin();
                   ///     System.out.println(isAdmin + "  "+ isOwner);
                        return (isOwner != null && isOwner) || (isAdmin != null && isAdmin);
                    });
        } catch (ApiException | ClientException e) {
            return false;
        }
    }
    public final void editChat(String title) {
        MessageChannel.editChat(title, peerId);
    }
    public final void sendMessage(@NotNull String msg) {
        MessageChannel.sendMessage(msg, peerId);
    }
    public final void sendAttachments(@NotNull String attachments) {
        MessageChannel.sendAttachments(attachments, peerId);
    }
    public final void sendMessage(String msg, @NotNull String attachments) {
        MessageChannel.sendMessage(msg, peerId, attachments);
    }

    public final void sendKeyboard(String msg, Keyboard keyboard) {
        MessageChannel.sendKeyboard(msg, peerId ,keyboard);
    }
    public final int chatId() {
        return peerId-2000000000;
    }

    public final T getUsers() {
        return container;
    }

}
