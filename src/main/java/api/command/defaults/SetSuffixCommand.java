package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.storage.User;
import api.utils.VkUtils;

public class SetSuffixCommand extends Command {
    public SetSuffixCommand() {
        super("setsuffix", "<пользователь> [суффикс]", "изменить суффикс пользователя");
    }

    @Override
    public final void execute(User sender, String[] args) {
        if (noPermission(sender)) {
            return;
        }
        String suffix = args[1];
        Integer id = null;
        if(args.length == 3) {
            id = VkUtils.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            suffix = args[2];
        } else if(args.length != 2) {
            return;
        }
        if(suffix.length() > 16) {
            sender.sendMessageFromChat("Слышь, дружок-пирожок, - большой суффикс");
            return;
        }
        if(id != null) {
            Bot.getProfileList().setSuffix(sender.getPeerId(), id, suffix);
        } else {
            Bot.getProfileList().setSuffix(sender, suffix);
        }
        sender.sendMessageFromChat("Вы успешно изменили суффикс");
    }
}
