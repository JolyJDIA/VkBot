package jolyjdia.bot.newcalculator.internal.lexer.tokens;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OperatorToken extends Token {
    @NonNls public final String operator;

    public OperatorToken(int position, String operator) {
        super(position);
        this.operator = operator;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'o';
    }

    @Override
    @Contract(pure = true)
    public final @NotNull String toString() {
        return "OperatorToken(" + operator + ')';
    }

}
