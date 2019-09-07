package jolyjdia.bot.game;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.NotNull;

public class GameCommand extends Command {
    GameCommand() {
        super("game");
        setPermission("roflanbot.game", "NO");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        if(sender.getPeerId() != 2000000001) {
            return;
        }
        if(args.length == 2) {
            int peerId = sender.getPeerId();
            if(args[1].equalsIgnoreCase("start")) {
                ObedientBot.sendKeyboard("ЙОБАНЫЙ РОТ ЭТОГО КАЗИНО, БЛЯТЬ", sender.getPeerId(),
                        new Keyboard().setButtons(GameKeyboard.GAME));
            } else if(args[1].equalsIgnoreCase("stop")) {
                GameManager.stop("close", peerId);
            } else if(args[1].equalsIgnoreCase("info")) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for(int value : GameManager.getScore(peerId)) {
                    ++i;
                    builder.append('#').append(i).append(' ').append("Очко: ").append(value).append('\n');
                }
                ObedientBot.sendMessage(builder.toString(), peerId);
            }
        }
    }
}
