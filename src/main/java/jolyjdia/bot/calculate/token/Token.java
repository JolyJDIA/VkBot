package jolyjdia.bot.calculate.token;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

public class Token {
    public static final Token BRACKET_LEFT = new Token("(");
    public static final Token BRACKET_RIGHT = new Token(")");
    public static final Token BRACKET_COMPLETE = new Token("()");

    @NonNls
    private String notation;

    @Contract(pure = true)
    public Token(String notation) {
        this.notation = notation;
    }

    @Contract(pure = true)
    public final String getNotation() {
        return notation;
    }

    public final void setNotation(String notation) {
        this.notation = notation;
    }
}
