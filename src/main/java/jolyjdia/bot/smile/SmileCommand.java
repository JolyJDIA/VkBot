package jolyjdia.bot.smile;

import api.command.Command;
import api.storage.User;
import api.utils.KeyboardUtils;
import org.jetbrains.annotations.NotNull;

public class SmileCommand extends Command {
    private final SmileLoad load;
    SmileCommand(SmileLoad load) {
        super("smile");
        this.load = load;
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.getChat().sendKeyboard("open", load.getKeyboard());
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("close")) {
                sender.getChat().sendKeyboard("close", KeyboardUtils.EMPTY_KEYBOARD);
            } else if(args[1].equalsIgnoreCase("load")) {
                if(!sender.isStaff()) {
                    sender.sendMessage("У вас нет прав");
                    return;
                }
                load.loadEmoticonsAlbum();
                sender.sendMessage("Смайлы обновлены!");
            }
        }
    }
}
