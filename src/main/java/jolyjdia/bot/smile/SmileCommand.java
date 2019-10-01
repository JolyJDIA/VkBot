package jolyjdia.bot.smile;

import api.Bot;
import api.command.Command;
import api.storage.User;
import api.utils.KeyboardUtils;
import org.jetbrains.annotations.NotNull;

public class SmileCommand extends Command {
    SmileCommand() {
        super("smile");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            Bot.sendKeyboard("open", sender.getPeerId(), SmileLoad.KEYBOARD);
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("close")) {
                Bot.sendKeyboard("close", sender.getPeerId(), KeyboardUtils.EMPTY_KEYBOARD);
            }
        }
    }
}
