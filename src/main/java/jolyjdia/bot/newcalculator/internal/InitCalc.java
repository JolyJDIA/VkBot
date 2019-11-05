package jolyjdia.bot.newcalculator.internal;

import api.Bot;
import api.command.Command;
import api.module.Module;
import api.storage.User;
import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.NonNls;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class InitCalc implements Module {

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new CalcCommand());
    }
    private static class CalcCommand extends Command {
        private static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        static {
            DECIMAL_FORMAT.applyPattern("#,##0.#####");
        }
        CalcCommand() {
            super("calc");
        }
        @Override
        public final void execute(@NonNls User sender, String[] args) {

            String list = Arrays.asList(args).subList(1, args.length).toString().substring(1);
            list = list.substring(0, list.length() -1);
            long start = System.currentTimeMillis();
            double result;
            try {
                result = Expression.compile(list).evaluate();
            } catch (ExpressionException e) {
                sender.sendMessageFromChat("Че за бред ты высрал?\n"+e.getMessage());
                return;
            }
            long end = System.currentTimeMillis() - start;
            sender.sendMessageFromChat(Double.isNaN(result) ? "NaN" : DECIMAL_FORMAT.format(result) + "\nВыполнилось за "+end+"ms");
        }
    }
}
