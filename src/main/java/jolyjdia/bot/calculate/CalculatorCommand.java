package jolyjdia.bot.calculate;

import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.NotNull;

public class CalculatorCommand extends Command {
    CalculatorCommand() {
        super("calc", "клавиатура калькулятора(РАБОТАЕТ ТОЛЬКО В ЛИЧНЫХ СООБЩЕНИЯХ)");
        setAlias("calculator");
    }

    @Override
    public final void execute(@NotNull User sender, String[] args) {
        int peerId = sender.getPeerId();
        if (peerId != sender.getUserId()) {
            return;
        }
        if(CalculatorManager.containsKey(peerId)) {
            CalculatorManager.closeCalculatorBoard("close", peerId);
        } else {
            CalculatorManager.openCalculatorBoard(peerId);
        }
    }
}
