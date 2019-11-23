package jolyjdia.bot.smile;

import api.command.Command;
import api.storage.User;
import api.utils.KeyboardUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class SmileCommand extends Command {
    private final SmileLoad load;
    SmileCommand(SmileLoad load) {
        super("smile");
        this.load = load;
        setPermission("roflansmile.load", "У тебя нет прав(");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            Bot.sendKeyboard("open", sender.getPeerId(), load.getKeyboard());
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("close")) {
                Bot.sendKeyboard("close", sender.getPeerId(), KeyboardUtils.EMPTY_KEYBOARD);
            } else if(args[1].equalsIgnoreCase("load")) {
                if(noPermission(sender)) {
                    return;
                }
                load.loadEmoticonsAlbum();
                sender.sendMessageFromChat("Смайлы обновлены!");
            }
        }
    }
}
