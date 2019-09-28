package jolyjdia.bot.calculate;

import api.Bot;
import api.event.EventLabel;
import api.event.EventPriority;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CalculatorRegister implements Module, Listener {
	private static final Pattern OUTPUT = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
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
		if(!OUTPUT.matcher(answer).matches()) {
			return;
		}
		e.getUser().sendMessageFromHisChat(answer);
	}
}
