package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class InfoUserCommand extends Command {
    public InfoUserCommand() {
        super("userinfo", "<пользователь>", "отображает информацию о пользователе");
        setAlias("infouser");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        @NonNls String info = "Ранг: ";
        if(args.length == 1) {
            info += sender.getGroup() + '\n' +
                    "Префикс: "+sender.getPrefix() + '\n' +
                    "Суффикс: "+ sender.getSuffix();
        } else if(args.length == 2) {
            Integer id = StringBind.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            User target = Bot.getProfileList().getUser(sender.getPeerId(), id);
            if(target == null) {
                ObedientBot.sendMessage("Не удалось найти этого пользователя в базе", sender.getPeerId());
                return;
            }
            info += target.getGroup() + '\n' +
                    "Префикс: "+target.getPrefix() + '\n' +
                    "Суффикс: "+ target.getSuffix();
        } else {
            return;
        }
        ObedientBot.sendMessage(info, sender.getPeerId());
    }
}
