package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.permission.PermissionManager;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class RankCommand extends Command {
    public RankCommand() {
        super("setrank", "<пользователь> <группа>", "изменить ранг пользователя");
        setPermission("roflanbot.rank", "У вас нет прав");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length == 3) {
            if (noPermission(sender)) {
                return;
            }
            Integer id = StringBind.getUserId(args[1], sender);
            if(id == null) {
                ObedientBot.sendMessage("Пользователя нет в беседе", sender.getPeerId());
                return;
            }
            if (PermissionManager.getPermGroup(args[2]) == null) {
                StringBuilder builder = new StringBuilder("Данного ранга не существует, возможные варианты:\n");
                for (String s : PermissionManager.getLookup().keySet()) {
                    builder.append(s).append('\n');
                }
                ObedientBot.sendMessage(builder.toString(), sender.getPeerId());
                return;
            }
            Bot.getProfileList().setRank(sender.getPeerId(), id, args[2]);
            ObedientBot.sendMessage("Вы успешно выдали права", sender.getPeerId());
        } else {
            ObedientBot.sendMessage("Использование: " + getUseCommand(), sender.getPeerId());
        }
    }
}
