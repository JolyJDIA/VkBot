package jolyjdia.bot.calculator.lexer.tokens;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class IdentifierToken extends Token {
    @NonNls public final String value;

    public IdentifierToken(int position, String value) {
        super(position);
        this.value = value;
    }

    @Override
    public final char id() {
        return 'i';
    }

    @Override
    public final @NotNull String toString() {
        return "IdentifierToken(" + value + ')';
    }

}
