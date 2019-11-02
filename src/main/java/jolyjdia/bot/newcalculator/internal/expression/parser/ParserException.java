package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ParserException extends ExpressionException {

    private static final long serialVersionUID = 2617185540625420491L;

    public ParserException(int position, @NonNls String message, Throwable cause) {
        super(position, getPrefix(position) + ": " + message, cause);
    }

    public ParserException(int position, String message) {
        super(position, getPrefix(position) + ": " + message);
    }

    @Contract(pure = true)
    private static @NotNull String getPrefix(int position) {
        return position < 0 ? "Parser error" : ("Parser error at " + (position + 1));
    }

}
