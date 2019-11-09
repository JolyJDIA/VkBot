package api.command.defaults;

import api.command.Command;
import api.permission.PermissionGroup;
import api.permission.PermissionManager;
import api.storage.User;
import api.utils.VkUtils;
import jolyjdia.bot.Bot;
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
        if (noPermission(sender)) {
            return;
        }
        if(args.length >= 4) {
            if (args[0].equalsIgnoreCase("addrank")) {
                PermissionManager.addGroup(args[1], args[2], ArrayUtils.subarray(args, 3, args.length));
                return;
            }
        }
        if (args.length == 3) {
            Integer id = VkUtils.getUserId(args[1], sender);
            if(id == null) {
                sender.sendMessageFromChat("Пользователя нет в беседе");
                return;
            }
            if (PermissionManager.getPermGroup(args[2]) == null) {
                StringBuilder builder = new StringBuilder("Данного ранга не существует, возможные варианты:\n");
                PermissionManager.getMapGroup().keySet().forEach(s -> builder.append(s).append('\n'));
                sender.sendMessageFromChat(builder.toString());
                return;
            }
            PermissionGroup group = PermissionManager.getPermGroup(args[2]);
            if(sender.getGroup() == group) {
                sender.sendMessageFromChat("У вас уже есть данный ранг");
                return;
            }
            Bot.getUserBackend().setRank(sender, group);
            sender.sendMessageFromChat("Вы успешно выдали права");
        } else {
            sender.sendMessageFromChat("Использование: " + getUseCommand());
        }
    }
}
