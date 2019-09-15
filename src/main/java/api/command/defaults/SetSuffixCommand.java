package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.StringBind;
import jolyjdia.bot.Loader;

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
            id = StringBind.getUserId(args[1], sender);
            if (id == null) {
                return;
            }
            suffix = args[2];
        } else if(args.length != 2) {
            return;
        }
        if(suffix.length() > 16) {
            sender.sendMessageFromHisChat("Слышь, дружок-пирожок, - большой суффикс");
            return;
        }
        if(id != null) {
            Loader.getProfileList().setSuffix(sender.getPeerId(), id, suffix);
        } else {
            Loader.getProfileList().setSuffix(sender, suffix);
        }
        sender.sendMessageFromHisChat("Вы успешно изменили суффикс");
    }
}
