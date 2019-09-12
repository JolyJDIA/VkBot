package jolyjdia.bot.calculate;

import api.JavaModule;
import api.command.RegisterCommandList;
import api.event.EventLabel;
import api.event.EventPriority;
import api.event.Listener;
import api.event.RegisterListEvent;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

public class CalculatorRegister extends JavaModule implements Listener {

	@Override
	public final void onLoad() {
        RegisterListEvent.registerEvent(this);
		RegisterCommandList.registerCommand(new CalculatorCommand());
    }
	@EventLabel(priority = EventPriority.HIGH)
	public static void onSend(@NotNull NewMessageEvent e) {
		Message msg = e.getMessage();
		String text = msg.getText();
		if(text.isEmpty()) {
			return;
		}
		int peerId = msg.getPeerId();
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
