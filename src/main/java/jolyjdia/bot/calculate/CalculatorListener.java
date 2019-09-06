package jolyjdia.bot.calculate;

import api.event.EventHandler;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

public class CalculatorListener implements Listener {
    @EventHandler
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        int peerId = msg.getPeerId();
        String text = msg.getText();
        if (CalculatorManager.isPersonalConversation(peerId, msg.getFromId())) {
            CalculatorManager.actionsCalculator(peerId, text);
            return;
        }
        if (!CalculatorManager.MATH.matcher(text).matches()) {
            return;
        }
        Calculator calculator = new Calculator(text);
        String answer = calculator.solveExpression();
        if(!CalculatorManager.OUTPUT.matcher(answer).matches()) {
            return;
        }
        ObedientBot.sendMessage(answer, peerId);
    }
}
