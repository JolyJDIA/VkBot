package jolyjdia.bot.newcalculator.expression.lexer.tokens;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CharacterToken extends Token {
    @NonNls private final char character;

    public CharacterToken(int position, char character) {
        super(position);
        this.character = character;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return character;
    }

    @Override
    @Contract(pure = true)
    public final @NotNull String toString() {
        return "CharacterToken(" + character + ')';
    }

}
