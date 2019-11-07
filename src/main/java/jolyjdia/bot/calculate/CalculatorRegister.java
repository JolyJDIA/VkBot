package jolyjdia.bot.calculate;

import api.Bot;
import api.event.EventLabel;
import api.event.EventPriority;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import jolyjdia.bot.calculate.calculator.Calculate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class CalculatorRegister implements Module, Listener {
	private static final Pattern OUTPUT = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
	private static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
	static {
		DECIMAL_FORMAT.applyPattern("#,##0.#####");
	}
	@Override
	public final void onLoad() {
		Bot.getBotManager().registerEvent(this);
		//Bot.getBotManager().registerCommand(new CalculatorCommand());
    }
	@EventLabel(priority = EventPriority.HIGH)
	public static void onSend(@NotNull NewMessageEvent e) {
		String text = e.getMessage().getText();
		if(text.isEmpty()) {
			return;
		}
	/*	int peerId = msg.getPeerId();
		if (CalculatorManager.isPersonalConversation(peerId, msg.getFromId())) {
			CalculatorManager.actionsCalculator(peerId, text);
			return;
		}*/
		if (!CalculatorManager.MATH.matcher(text).matches()) {
			return;
		}
		long start = System.currentTimeMillis();
		@NonNls String answer = new Calculate(text).solveExpression();
		long end = System.currentTimeMillis() - start;
		if(!OUTPUT.matcher(answer).matches()) {
			return;
		}
		e.getUser().sendMessageFromChat(answer+"\nВыполнено за: "+end+"ms");
	}
}
