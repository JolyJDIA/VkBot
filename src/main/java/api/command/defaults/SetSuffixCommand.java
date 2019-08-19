package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class SetSuffixCommand extends Command {
    public SetSuffixCommand() {
        super("setsuffix", "[Адрес пользователя] <Суффикс>", "изменить суффикс пользователя");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 2) {
            Bot.getProfileList().setSuffix(sender, args[1]);
            ObedientBot.sendMessage("Вы успешно изменили суффикс", sender.getPeerId());
        } else if(args.length == 3) {
            Integer id = StringBind.getUserId(args[1], sender);
            if(id == null) {
                ObedientBot.sendMessage("Пользователя нет в беседе", sender.getPeerId());
                return;
            }
            Bot.getProfileList().setSuffix(sender.getPeerId(), id, args[2]);
            ObedientBot.sendMessage("Вы успешно изменили суффикс", sender.getPeerId());
        }
    }
}
