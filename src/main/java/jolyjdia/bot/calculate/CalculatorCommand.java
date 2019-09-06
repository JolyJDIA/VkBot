package jolyjdia.bot.calculate;

import api.command.Command;
import api.entity.User;
import org.jetbrains.annotations.NotNull;

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
        if(CalculatorManager.containsKey(peerId)) {
            CalculatorManager.closeCalculatorBoard("_", peerId);
            return;
        }
        CalculatorManager.openCalculatorBoard("Калькулятор", peerId);
    }
}
