package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;

public class SetPrefixCommand extends Command {
    public SetPrefixCommand() {
        super("setprefix", "[пользователь] <префикс>", "изменить префикс пользователя");
    }

    @Override
    public final void execute(User sender, String[] args) {
        if (noPermission(sender)) {
            return;
        }
        if (args.length == 2) {
            Bot.getProfileList().setPrefix(sender, args[1]);
            ObedientBot.sendMessage("Вы успешно изменили префикс", sender.getPeerId());
        } else if (args.length == 3) {
            Integer id = StringBind.getUserId(args[1], sender);
            if (id == null) {
                ObedientBot.sendMessage("Пользователя нет в беседе", sender.getPeerId());
                return;
            }
            Bot.getProfileList().setPrefix(sender.getPeerId(), id, args[2]);
            ObedientBot.sendMessage("Вы успешно изменили префикс", sender.getPeerId());
        }
    }
}