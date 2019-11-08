package api.utils;

import api.storage.User;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.ConversationMember;
import com.vk.api.sdk.objects.messages.responses.GetConversationMembersResponse;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class VkUtils {
    @Contract(pure = true)
    private VkUtils() {}

    public static @Nullable Integer getUserId(@NotNull String s, @NotNull User sender) {
        try {
            int id = s.charAt(0) == '[' ? getIdNick(s) : getIdString(s);
            GetConversationMembersResponse g = Bot.getVkApiClient()
                    .messages()
                    .getConversationMembers(Bot.getGroupActor(), sender.getPeerId())
                    .execute();
            Optional<ConversationMember> member = g.getItems().stream()
                    .filter(m -> m.getMemberId() == id).findFirst();
            return member.map(ConversationMember::getMemberId).orElse(null);
        } catch (ApiException | ClientException e) {
            sender.sendMessageFromChat("Пользователя нет в беседе");
        }
        return null;
    }
    public static int getIdNick(@NotNull String a) {
        return Integer.parseInt(a.substring(3).split("\\|")[0]);
    }

    public static int getIdString(String a) {
        return Integer.parseInt(a);
    }
}
