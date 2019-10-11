package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.permission.PermissionManager;
import api.storage.User;
import api.utils.StringBind;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RankCommand extends Command {
    public RankCommand() {
        super("setrank", "<пользователь> <группа>", "изменить ранг пользователя");
        setPermission("roflanbot.rank", "У вас нет прав");
        setAlias("addrank");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if(args.length >= 4) {
            if (args[0].equalsIgnoreCase("addrank")) {
                PermissionManager.addGroup(args[1], args[2], ArrayUtils.subarray(args, 3, args.length));
                return;
            }
        }
        if (args.length == 3) {
            if (noPermission(sender)) {
                return;
            }
            Integer id = StringBind.getUserId(args[1], sender);
            if(id == null) {
                sender.sendMessageFromHisChat("Пользователя нет в беседе");
                return;
            }
            if (PermissionManager.getPermGroup(args[2]) == null) {
                StringBuilder builder = new StringBuilder("Данного ранга не существует, возможные варианты:\n");
                for (String s : PermissionManager.getMapGroup().keySet()) {
                    builder.append(s).append('\n');
                }
                sender.sendMessageFromHisChat(builder.toString());
                return;
            }
            Bot.getProfileList().setRank(sender.getPeerId(), id, PermissionManager.getPermGroup(args[2]));
            sender.sendMessageFromHisChat("Вы успешно выдали права");
        } else {
            sender.sendMessageFromHisChat("Использование: " + getUseCommand());
        }
    }
}
