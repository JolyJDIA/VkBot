package jolyjdia.bot.kubanoid;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.NotNull;

public class KubanoidCommand extends Command {
    KubanoidCommand() {
        super("game");
        setPermission("roflanbot.game", "NO");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        if(args.length == 2) {
            if(noPermission(sender)) {
                return;
            }
            int peerId = sender.getPeerId();
            if(args[1].equalsIgnoreCase("start")) {
                ObedientBot.sendKeyboard("ЙОБАНЫЙ РОТ ЭТОГО КАЗИНО, БЛЯТЬ", sender.getPeerId(),
                        new Keyboard().setButtons(KubanoidKeyboard.GAME));

            } else if(args[1].equalsIgnoreCase("stop")) {
                KubanoidManager.stopGame(peerId);

            } else if(args[1].equalsIgnoreCase("info")) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for(KubanoidPlayer value : KubanoidManager.getScore(peerId)) {
                    ++i;
                    builder.append('#').append(i).append(' ').append("Очко: ").append(value.getScore()).append('\n');
                }
                ObedientBot.sendMessage(builder.toString(), peerId);
            }
        }
    }
}
