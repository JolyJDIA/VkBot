package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class InfoUserCommand extends Command {
    public InfoUserCommand() {
        super("userinfo", "<пользователь>", "отображает информацию о пользователе");
        setAlias("infouser");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        String info;
        if(args.length == 1) {
            info = getInfo(sender.getGroup(), sender.getPrefix(), sender.getSuffix());
        } else if(args.length == 2) {
            Integer id = StringBind.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            User target = Bot.getProfileList().getUser(sender.getPeerId(), id);
            if(target == null) {
                sender.sendMessageFromHisChat("Не удалось найти этого пользователя в базе");
                return;
            }

            info = getInfo(target.getGroup(), target.getPrefix(), target.getSuffix());
        } else {
            return;
        }
        sender.sendMessageFromHisChat(info);
    }
    @NotNull
    @Contract(pure = true)
    @NonNls private static String getInfo(String group, String prefix, String suffix) {
        return "Ранг: "+group + '\n' +
                "Префикс: "+prefix + '\n' +
                "Суффикс: "+ suffix;
    }
}
