package jolyjdia.bot.calculate;

import api.event.EventHandler;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CalculatorListener implements Listener {
    private static final Pattern MATH = Pattern.compile("(.*[0-9]+.*)");
    private static final Pattern MATHPERSONAL = Pattern.compile("[a-zA-Z.\\d+\\-*/()^<= ]*");
    @EventHandler
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        int peerId = msg.getPeerId();
        boolean personal = CalculatorManager.isPersonalConversation(peerId, msg.getFromId());
        if (personal) {
            if(!MATHPERSONAL.matcher(msg.getText()).matches()) {
                CalculatorManager.closeCalculatorBoard("Я вижу, дружок, тебе не нужен калькулятор-board", peerId);
                return;
            }
            CalculatorManager.actionsCalculator(peerId, msg.getText());
        } else {
            if (!MATH.matcher(msg.getText()).matches()) {
                return;
            }
            Calculator calc = new Calculator(msg.getText());
            String answer = calc.solveExpression();
            if (answer.isEmpty()) {
                return;
            }
            ObedientBot.sendMessage(answer, peerId);
        }
    }
}
