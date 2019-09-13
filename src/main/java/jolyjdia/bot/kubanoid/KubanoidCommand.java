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
                ObedientBot.sendKeyboard("ЙОБАНЫЙ РОТ ЭТОГО КАЗИНО, БЛЯТЬ", sender.getUserId(),
                        new Keyboard().setButtons(KubanoidKeyboard.GAME));

            } else if(args[1].equalsIgnoreCase("stop")) {
                KubanoidManager.stopGame("Конец игры!", sender.getUserId());

            } else if(args[1].equalsIgnoreCase("info")) {
                ObedientBot.sendMessage(KubanoidManager.getInfo(peerId), peerId);
            }
        }
    }
}
