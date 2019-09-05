package jolyjdia.bot.calculate;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class CalculatorCommand extends Command {
    CalculatorCommand() {
        super("calc");
    }

    @Override
    public final void execute(@NotNull User sender, String[] args) {
        int peerId = sender.getPeerId();
        if (peerId != sender.getUserId()) {
            return;
        }
        if(CalculatorListener.containsKey(peerId)) {
            CalculatorListener.removeHistory(peerId);
            ObedientBot.sendKeyboard("Калькулятор", peerId, new Keyboard()
                            .setButtons(Collections.emptyList())
                            .setOneTime(false));
            return;
        }
        CalculatorListener.addHistory(peerId);
        ObedientBot.sendKeyboard("Калькулятор", peerId, new Keyboard()
                        .setButtons(CalculatorKeyboard.BOARD)
                        .setOneTime(false));
    }
}
