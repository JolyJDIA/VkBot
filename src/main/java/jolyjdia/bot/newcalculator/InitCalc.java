package jolyjdia.bot.newcalculator;

import api.command.Command;
import api.module.Module;
import api.storage.User;
import jolyjdia.bot.Bot;
import jolyjdia.bot.newcalculator.expression.Expression;
import jolyjdia.bot.newcalculator.expression.ExpressionException;
import org.jetbrains.annotations.NonNls;

import java.util.Arrays;

public class InitCalc implements Module {

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new CalcCommand());
    }
    private static class CalcCommand extends Command {
        CalcCommand() {
            super("calc");
        }
        @Override
        public final void execute(@NonNls User sender, String[] args) {

            String list = Arrays.asList(args).subList(1, args.length).toString().substring(1);
            list = list.substring(0, list.length() -1);
            String result;
            try {
                result = Expression.compile(list).evaluate();
            } catch (ExpressionException e) {
                sender.sendMessageFromChat("Че за бред ты высрал?\n"+e.getMessage());
                return;
            }
            if(result.isEmpty()) {
                return;
            }
            sender.sendMessageFromChat(result);
        }
    }
}
