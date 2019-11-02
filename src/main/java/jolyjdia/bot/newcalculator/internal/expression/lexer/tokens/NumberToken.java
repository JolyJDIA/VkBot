package jolyjdia.bot.newcalculator.internal.expression.lexer.tokens;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NumberToken extends Token {
    @NonNls public final double value;

    public NumberToken(int position, double value) {
        super(position);
        this.value = value;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return '0';
    }

    @Override
    @Contract(pure = true)
    public final @NotNull String toString() {
        return "NumberToken(" + value + ')';
    }

}
