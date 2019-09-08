package jolyjdia.bot.game;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class GameCommand extends Command {
    GameCommand() {
        super("game");
        setPermission("roflanbot.game", "NO");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        if(args.length == 2) {
            int peerId = sender.getPeerId();
            if(args[1].equalsIgnoreCase("start")) {
                ObedientBot.sendKeyboard("ЙОБАНЫЙ РОТ ЭТОГО КАЗИНО, БЛЯТЬ", sender.getPeerId(),
                        new Keyboard().setButtons(GameKeyboard.GAME));

            } else if(args[1].equalsIgnoreCase("stop")) {
                ObedientBot.sendKeyboard("Конец игры!", peerId, new Keyboard().setButtons(Collections.emptyList()));
                GameManager.map.get(peerId).clear();

            } else if(args[1].equalsIgnoreCase("info")) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for(Player value : GameManager.getScore(peerId)) {
                    ++i;
                    builder.append('#').append(i).append(' ').append("Очко: ").append(value.getScore()).append('\n');
                }
                ObedientBot.sendMessage(builder.toString(), peerId);
            }
        }
    }
}
