package jolyjdia.bot.calculator.lexer.tokens;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NumberToken extends Token {
    @NonNls public final double value;

    public NumberToken(int position, double value) {
        super(position);
        this.value = value;
    }

    @Override
    public final char id() {
        return '0';
    }

    @Override
    public final @NotNull String toString() {
        return "NumberToken(" + value + ')';
    }

}
