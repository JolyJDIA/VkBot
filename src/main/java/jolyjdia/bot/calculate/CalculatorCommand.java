package jolyjdia.bot.calculate;

import api.command.Command;
import api.entity.User;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class CalculatorCommand extends Command {
    public static boolean board;
    CalculatorCommand() {
        super("calc");
    }

    @Override
    public final void execute(@NotNull User sender, String[] args) {
        board = !board;
        try { Bot.getVkApiClient().messages()
                .send(Bot.getGroupActor())
                .randomId(310289867)
                .message("j")
                .groupId(Bot.GROUP_ID)
                .peerId(sender.getPeerId())
                .keyboard(new Keyboard()
                        .setButtons(CalculatorKeyboard.BOARD)
                        .setOneTime(false)
                )
                .execute();
        } catch (ApiException | ClientException ignored) {}
    }
}
