package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.permission.PermissionManager;
import api.utils.ObedientBot;
import api.utils.StringBind;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.ConversationMember;
import com.vk.api.sdk.objects.messages.responses.GetConversationMembersResponse;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RankCommand extends Command {
    public RankCommand() {
        super("/rank set <Адрес пользователя> <Группа>\n/rank setprefix <Адрес пользователя> <Префикс>\n/rank setsuffix <Адрес пользователя> <Суффикс>\n",
                "изменить профиль пользователя"
        );
        setAlias("rank");
        setPermission("roflanbot.rank", "У вас нет прав");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 4) {
            if (noPermission(sender)) {
                return;
            }
            Integer id = getUserId(args[2], sender);
            if(id == null) {
                ObedientBot.sendMessage("Пользователя нет в беседе", sender.getPeerId());
                return;
            }
            if (args[1].equalsIgnoreCase("set")) {
                if (PermissionManager.getPermGroup(args[3]) == null) {
                    StringBuilder builder = new StringBuilder("Данного ранга не существует, возможные варианты:\n");
                    for (String s : PermissionManager.getLookup().keySet()) {
                        builder.append(s).append('\n');
                    }
                    ObedientBot.sendMessage(builder.toString(), sender.getPeerId());
                    return;
                }
                Bot.getProfileList().setRank(sender.getPeerId(), id, args[3]);
                ObedientBot.sendMessage("Вы успешно выдали права", sender.getPeerId());

            } else if (args[1].equalsIgnoreCase("setprefix")) {
                Bot.getProfileList().setPrefix(sender.getPeerId(), id, args[3]);
                ObedientBot.sendMessage("Вы успешно изменили префикс", sender.getPeerId());

            } else if (args[1].equalsIgnoreCase("setsuffix")) {
                Bot.getProfileList().setSuffix(sender.getPeerId(), id, args[3]);
                ObedientBot.sendMessage("Вы успешно изменили суффикс", sender.getPeerId());
            }
        } else {
            ObedientBot.sendMessage("Использование: " + getUsageMessage(), sender.getPeerId());
        }
    }
    @Nullable
    public static Integer getUserId(@NotNull String s, @NotNull User sender) {
        try {
            int id = s.charAt(0) == '[' ? StringBind.getIdNick(s) : StringBind.getIdString(s);

            GetConversationMembersResponse g = Bot.getVkApiClient().
                    messages().getConversationMembers(Bot.getGroupActor(), sender.getPeerId()).execute();
            if (g == null) {
                ObedientBot.sendMessage("Упс, что-то пошло не так;( Обратитесь к администратору", sender.getPeerId());
                return null;
            }
            Optional<ConversationMember> member = g.getItems().stream().filter(m ->
                    m.getMemberId() == id).findFirst();
            return member.map(ConversationMember::getMemberId).orElse(null);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            ObedientBot.sendMessage("Некорректный адрес пользователя", sender.getPeerId());
        }
        return null;
    }
}
