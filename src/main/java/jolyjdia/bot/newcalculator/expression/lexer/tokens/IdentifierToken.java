package jolyjdia.bot.newcalculator.expression.lexer.tokens;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class IdentifierToken extends Token {
    @NonNls public final String value;

    public IdentifierToken(int position, String value) {
        super(position);
        this.value = value;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'i';
    }

    @Override
    @Contract(pure = true)
    public final @NotNull String toString() {
        return "IdentifierToken(" + value + ')';
    }

}
