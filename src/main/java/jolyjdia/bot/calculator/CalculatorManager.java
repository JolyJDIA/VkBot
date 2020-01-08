package jolyjdia.bot.calculator;

import jolyjdia.api.event.EventLabel;
import jolyjdia.api.event.Listener;
import jolyjdia.api.event.messages.NewMessageEvent;
import jolyjdia.api.module.Module;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

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
            e.getChat().sendMessage(reply);
        } catch (ExpressionException ex) {}
    }
}
