package jolyjdia.api.command.defaults;

import jolyjdia.api.command.Command;
import jolyjdia.api.permission.PermissionGroup;
import jolyjdia.api.permission.PermissionManager;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.VkUtils;
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
            if (PermissionManager.getPermGroup(args[2]) == null) {
                StringBuilder builder = new StringBuilder("Данного ранга не существует, возможные варианты:\n");
                PermissionManager.ranks().forEach(s -> builder.append(s).append('\n'));
                sender.getChat().sendMessage(builder.toString());
                return;
            }
            VkUtils.getUserId(args[1]).ifPresentOrElse(id -> {
                User target = Bot.getUserBackend().addIfAbsentAndReturnUser(sender.getPeerId(), id);
                PermissionGroup group = PermissionManager.getPermGroup(args[2]);
                if(sender.getGroup() == group) {
                    sender.getChat().sendMessage("У него уже есть данный ранг");
                } else {
                    target.setGroup(group);
                    sender.getChat().sendMessage("Вы успешно выдали права");
                }
            }, () -> sender.getChat().sendMessage("Данного пользователя нет в беседе"));
        } else {
            sender.getChat().sendMessage("Использование: " + getUseCommand());
        }
    }
}
