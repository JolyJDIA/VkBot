package jolyjdia.bot.calculate;

import api.Bot;
import api.event.EventLabel;
import api.event.EventPriority;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import jolyjdia.bot.calculate.calculator.Calculate;
import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
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
    public static void main(String[] args) throws ExpressionException {
		long start = System.currentTimeMillis();
		double result = Expression.compile("(((5-442)-(5+4-1)*2)-1+0.5^2-7+pi^3-(sin(20)*cos(30+pi)+5-2)*574)-1*1024-1023+45455-0.554+0.54-3.14").evaluate();
		long end = System.currentTimeMillis() -start;
		System.out.println("Ответ: " + result + "\nВермя выполнения: " + end + "ms");
		/**
		 * 1)131ms //10+51
		 * 2)69ms //sin(20)
		 * 3)46ms //pi^2+(5-2)+4-6/3
		 * 4)49ms //(((5-442)-(5+4-1)*2)-1+0.5^2-7+pi^3-(sin(20)*cos(30+pi)+5-2)*574)-1
		 * 5)48ms //(((5-442)-(5+4-1)*2)-1+0.5^2-7+pi^3-(sin(20)*cos(30+pi)+5-2)*574)-1*1024-1023+45455-0.554+0.54-3.14
		 */
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
		Calculate calculator = new Calculate(text);
		String answer = calculator.solveExpression();
		if(!OUTPUT.matcher(answer).matches()) {
			return;
		}
		long end = System.currentTimeMillis() - start;
		System.out.println(end);
		e.getUser().sendMessageFromChat(answer);
	}
}
