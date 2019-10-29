/*
package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.storage.User;
import api.utils.VkUtils;

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
            id = VkUtils.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            prefix = args[2];
        } else if(args.length != 2) {
            return;
        }
        if(prefix.length() > 16) {
            sender.sendMessageFromChat("Слышь, дружок-пирожок, - большой префикс");
            return;
        }
        if(id != null) {
            Bot.getProfileList().setPrefix(sender.getPeerId(), id, prefix);
        } else {
            Bot.getProfileList().setPrefix(sender, prefix);
        }
        sender.sendMessageFromChat("Вы успешно изменили префикс");
    }
}*/
