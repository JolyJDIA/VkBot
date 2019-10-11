package api.utils;

import api.Bot;
import api.storage.User;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.ConversationMember;
import com.vk.api.sdk.objects.messages.responses.GetConversationMembersResponse;
import jolyjdia.bot.Loader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class StringBind {

    @Contract(pure = true)
    private StringBind() {}
    public static @NotNull String toString(int start, @NotNull String[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = start; ; ++i) {
            builder.append(a[i]);
            if (i == iMax) {
                return builder.toString();
            }
            builder.append(' ');
        }
    }

    public static @NotNull String toString(@NotNull String[] a) {
        return toString(1, a);
    }

    public static @Nullable Integer getUserId(@NotNull String s, @NotNull User sender) {
        try {
            int id = s.charAt(0) == '[' ? getIdNick(s) : getIdString(s);
            GetConversationMembersResponse g = Loader.getVkApiClient()
                    .messages()
                    .getConversationMembers(Bot.getGroupActor(), sender.getPeerId())
                    .execute();
            Optional<ConversationMember> member = g.getItems().stream()
                    .filter(m -> m.getMemberId() == id).findFirst();
            return member.map(ConversationMember::getMemberId).orElse(null);
        } catch (ApiException | ClientException e) {
            sender.sendMessageFromHisChat("Пользователя нет в беседе");
        }
        return null;
    }
    /**
     * @param a
     * @return id
     * @throws NumberFormatException
     */
    public static int getIdNick(@NotNull String a) {
        return Integer.parseInt(a.substring(3).split("\\|")[0]);
    }

    /**
     * @param a
     * @return id
     * @throws NumberFormatException
     */
    public static int getIdString(String a) {
        return Integer.parseInt(a);
    }
}
