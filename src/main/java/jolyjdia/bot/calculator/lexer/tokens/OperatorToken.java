package jolyjdia.bot.calculator.lexer.tokens;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OperatorToken extends Token {
    @NonNls public final String operator;

    public OperatorToken(int position, String operator) {
        super(position);
        this.operator = operator;
    }

    @Override
    public final char id() {
        return 'o';
    }

    @Override
    public final @NotNull String toString() {
        return "OperatorToken(" + operator + ')';
    }

}
