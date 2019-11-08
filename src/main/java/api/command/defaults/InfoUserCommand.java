package api.command.defaults;

import api.command.Command;
import api.permission.PermissionGroup;
import api.storage.User;
import api.utils.VkUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class InfoUserCommand extends Command {
    public InfoUserCommand() {
        super("userinfo", "<пользователь>", "отображает информацию о пользователе");
        setAlias("infouser");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        @NonNls String info = "Айди-беседа: "+sender.getPeerId()+ '\n';
        if(args.length == 1) {
            info += getInfo(sender.getGroup());
        } else if(args.length == 2) {
            Integer id = VkUtils.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            User target = Bot.getUserBackend().getUser(sender.getPeerId(), id);
            if(target == null) {
                sender.sendMessageFromChat("Не удалось найти этого пользователя в базе");
                return;
            }

            info += getInfo(target.getGroup());
        } else {
            return;
        }
        sender.sendMessageFromChat(info);
    }
    @NonNls
    @Contract(pure = true)
    private static @NotNull String getInfo(@NotNull PermissionGroup group) {
        return "Ранг: "+group.getName() + '\n' +
                "Префикс: "+group.getPrefix() + '\n' +
                "Суффикс: "+ group.getSuffix();
    }
}
