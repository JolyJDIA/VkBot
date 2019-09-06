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
        boolean personal = CalculatorManager.isPersonalConversation(peerId, msg.getFromId());
        if (personal) {
            if(!CalculatorManager.MATHPERSONAL.matcher(msg.getText()).matches()) {
                CalculatorManager.closeCalculatorBoard("Я вижу, дружок, тебе не нужен калькулятор", peerId);
                return;
            }
            CalculatorManager.actionsCalculator(peerId, msg.getText());
        } else {
            if (!CalculatorManager.MATH.matcher(msg.getText()).matches()) {
                return;
            }
            long start = System.nanoTime();
            Calculator calc = new Calculator(msg.getText());
            String answer = calc.solveExpression();
            long end = System.nanoTime() - start;
            System.out.println(end);
            if (answer.isEmpty()) {
                return;
            }
            if(!CalculatorManager.NUMBER.matcher(answer).matches()) {
                return;
            }
            ObedientBot.sendMessage(answer, peerId);
        }
    }
}
