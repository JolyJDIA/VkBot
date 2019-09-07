package api.utils;

import api.entity.User;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.ConversationMember;
import com.vk.api.sdk.objects.messages.responses.GetConversationMembersResponse;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class StringBind {

    @Contract(pure = true)
    private StringBind() {}

    @NotNull
    public static String toString(@NotNull String[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; ; ++i) {
            builder.append(a[i]);
            if (i == iMax) {
                return builder.toString();
            }
            builder.append(' ');
        }
    }

    @Nullable
    public static Integer getUserId(@NotNull String s, @NotNull User sender) {
        try {
            int id = s.charAt(0) == '[' ? getIdNick(s) : getIdString(s);
            GetConversationMembersResponse g = Bot.getVkApiClient().
                    messages().getConversationMembers(Bot.getGroupActor(), sender.getPeerId()).execute();
            System.out.println(g);
            if (g == null) {
                ObedientBot.sendMessage("Упс, что-то пошло не так;( Обратитесь к администратору", sender.getPeerId());
                return null;
            }
            Optional<ConversationMember> member = g.getItems().stream().filter(m ->
                    m.getMemberId() == id).findFirst();
            return member.map(ConversationMember::getMemberId).orElse(null);
        } catch (ApiException | ClientException e) {
            ObedientBot.sendMessage("Пользователя нет в беседе", sender.getPeerId());
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
