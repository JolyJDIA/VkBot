package jolyjdia.bot.smile;

import api.command.Command;
import api.storage.User;
import api.utils.KeyboardUtils;
import api.utils.text.MessageReceiver;
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
            MessageReceiver.sendKeyboard("open", sender.getPeerId(), load.getKeyboard());
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("close")) {
                MessageReceiver.sendKeyboard("close", sender.getPeerId(), KeyboardUtils.EMPTY_KEYBOARD);
            } else if(args[1].equalsIgnoreCase("load")) {
                if(!STAFF_ADMIN.containsValue(sender.getUserId())) {
                    sender.sendMessage("У вас нет прав");
                    return;
                }
                load.loadEmoticonsAlbum();
                sender.sendMessage("Смайлы обновлены!");
            }
        }
    }
}
