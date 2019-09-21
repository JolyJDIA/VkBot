package jolyjdia.bot.kubanoid;

import api.Bot;
import api.command.Command;
import api.storage.User;
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
                //TODO:
                Bot.sendKeyboard("ЙОБАНЫЙ РОТ ЭТОГО КАЗИНО, БЛЯТЬ", sender.getUserId(),
                        new Keyboard().setButtons(KubanoidKeyboard.GAME));

            } else if(args[1].equalsIgnoreCase("stop")) {
                KubanoidManager.stopGame("Конец игры!", sender.getUserId());

            } else if(args[1].equalsIgnoreCase("info")) {
                Bot.sendMessage(KubanoidManager.getInfo(peerId), peerId);
            }
        }
    }
}
