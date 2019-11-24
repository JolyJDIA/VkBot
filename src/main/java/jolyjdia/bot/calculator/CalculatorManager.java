package jolyjdia.bot.calculator;

import api.command.Command;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import api.storage.User;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

public class CalculatorManager implements Module, Listener {
    private static final Pattern MATH = Pattern.compile("[a-zA-Z.\\d+\\-*/()^ ]*");
    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
    }
    @EventLabel
    public static void onMsg(@NotNull NewMessageEvent e) {
        String text = e.getMessage().getText();
        if (text == null || text.isEmpty()) {
            return;
        }
        if(!MATH.matcher(text).matches()) {
            return;
        }
        text = text.toLowerCase(Locale.ENGLISH);
        try {
            String reply = Expression.compile(text).evaluate();
            if(reply.isEmpty()) {
                return;
            }
            e.getUser().sendMessage(reply);
        } catch (ExpressionException ex) {}
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
                sender.sendMessage("Че за бред ты высрал?\n"+e.getMessage());
                return;
            }
            if(result.isEmpty()) {
                return;
            }
            sender.sendMessage(result);
        }
    }
}
