package jolyjdia.bot.calculator.lexer.tokens;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CharacterToken extends Token {
    @NonNls private final char character;

    public CharacterToken(int position, char character) {
        super(position);
        this.character = character;
    }

    @Override
    public final char id() {
        return character;
    }

    @Override
    public final @NotNull String toString() {
        return "CharacterToken(" + character + ')';
    }

}
