package jolyjdia.bot.kubanoid;

import api.Bot;
import api.command.Command;
import api.storage.User;
import api.utils.KeyboardUtils;
import com.google.common.collect.ImmutableList;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KubanoidCommand extends Command {
    private static final List<List<KeyboardButton>> KEYBOARD = ImmutableList.of(
            ImmutableList.of(
                    KeyboardUtils.create("КИНУТЬ", KeyboardButtonColor.POSITIVE),
                    KeyboardUtils.create("УЧАСТВОВАТЬ", KeyboardButtonColor.PRIMARY)
            )
    );
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
                Bot.sendKeyboard("ЙОБАНЫЙ РОТ ЭТОГО КАЗИНО, БЛЯТЬ", sender.getUserId(),
                        new Keyboard().setButtons(KEYBOARD));

            } else if(args[1].equalsIgnoreCase("stop")) {
                KubanoidManager.stopGame("Конец игры!", sender.getUserId());

            } else if(args[1].equalsIgnoreCase("info")) {
                Bot.sendMessage(KubanoidManager.getInfo(peerId), peerId);
            }
        }
    }
}
