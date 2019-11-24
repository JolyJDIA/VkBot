package jolyjdia.bot.calculate;

import api.event.EventLabel;
import api.event.EventPriority;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import jolyjdia.bot.Bot;
import jolyjdia.bot.calculate.calculator.Calculate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CalculatorRegister implements Module, Listener {
	private static final Pattern OUTPUT = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
	private static final Pattern MATH = Pattern.compile("[a-zA-Z.\\d+\\-*/()^ ]*");
	@Override
	public final void onLoad() {
		Bot.getBotManager().registerEvent(this);
    }
	@EventLabel(priority = EventPriority.HIGH)
	public static void onSend(@NotNull NewMessageEvent e) {
		String text = e.getMessage().getText();
		if(text.isEmpty()) {
			return;
		}
		if (!MATH.matcher(text).matches()) {
			return;
		}
		@NonNls String answer = new Calculate(text).solveExpression();
		if(!OUTPUT.matcher(answer).matches()) {
			return;
		}
		e.getUser().sendMessage(answer);
	}
}
