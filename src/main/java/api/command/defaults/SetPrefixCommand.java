package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.entity.User;
import api.utils.StringBind;

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
            sender.sendMessageFromHisChat("Слышь, дружок-пирожок, - большой префикс");
            return;
        }
        if(id != null) {
            Bot.getProfileList().setPrefix(sender.getPeerId(), id, prefix);
        } else {
            Bot.getProfileList().setPrefix(sender, prefix);
        }
        sender.sendMessageFromHisChat("Вы успешно изменили префикс");
    }
}