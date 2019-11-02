package jolyjdia.bot.newcalculator.internal.expression.lexer;

import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

class LexerException extends ExpressionException {

    private static final long serialVersionUID = -8991266270731164630L;

    LexerException(int position, @NonNls String message, Throwable cause) {
        super(position, getPrefix(position) + ": " + message, cause);
    }

    public LexerException(int position, String message) {
        super(position, getPrefix(position) + ": " + message);
    }

    @Contract(pure = true)
    private static @NotNull String getPrefix(int position) {
        return position < 0 ? "Lexer error" : ("Lexer error at " + (position + 1));
    }

}
