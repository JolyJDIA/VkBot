package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;

public class SetSuffixCommand extends Command {
    public SetSuffixCommand() {
        super("setsuffix", "[пользователь] <суффикс>", "изменить суффикс пользователя");
    }

    @Override
    public final void execute(User sender, String[] args) {
        if (noPermission(sender)) {
            return;
        }
        String suffix = args[1];
        Integer id = null;
        if(args.length == 3) {
            id = StringBind.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            suffix = args[2];
        } else if(args.length != 2) {
            return;
        }
        if(suffix.length() > 16) {
            ObedientBot.sendMessage("Слышь, дружок-пирожок, - большой суффикс", sender.getPeerId());
            return;
        }
        if(id != null) {
            Bot.getProfileList().setSuffix(sender.getPeerId(), id, suffix);
        } else {
            Bot.getProfileList().setSuffix(sender, suffix);
        }
        ObedientBot.sendMessage("Вы успешно изменили суффикс", sender.getPeerId());
    }
}
