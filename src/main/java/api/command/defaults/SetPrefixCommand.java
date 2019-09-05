package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;

public class SetPrefixCommand extends Command {
    public SetPrefixCommand() {
        super("setprefix", "<пользователь> [префикс]", "изменить префикс пользователя");
    }
    @Override
    public final void execute(User sender, String[] args) {
        if (noPermission(sender)) {
            return;
        }
        String prefix = args[1];
        Integer id = null;
        if(args.length == 3) {
            id = StringBind.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            prefix = args[2];
        } else if(args.length != 2) {
            return;
        }
        if(prefix.length() > 16) {
            ObedientBot.sendMessage("Слышь, дружок-пирожок, - большой префикс", sender.getPeerId());
            return;
        }
        if(id != null) {
            Bot.getProfileList().setPrefix(sender.getPeerId(), id, prefix);
        } else {
            Bot.getProfileList().setPrefix(sender, prefix);
        }
        ObedientBot.sendMessage("Вы успешно изменили префикс", sender.getPeerId());
    }
}