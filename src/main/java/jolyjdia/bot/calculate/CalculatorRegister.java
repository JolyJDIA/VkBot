package jolyjdia.bot.calculate;

import api.Bot;
import api.event.EventLabel;
import api.event.EventPriority;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.event.messages.SendCommandEvent;
import api.module.Module;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

public class CalculatorRegister implements Module, Listener {

	@Override
	public final void onLoad() {
		Bot.getBotManager().registerEvent(this);
		Bot.getBotManager().registerCommand(new CalculatorCommand());
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
		e.getUser().sendMessageFromHisChat(answer);
	}
	@EventLabel
	public static void onCommand(@NotNull SendCommandEvent e) {
		if(e.getArguments()[0].equalsIgnoreCase("/help1")) {
			e.setCancelled(true);
		}
	}
}
