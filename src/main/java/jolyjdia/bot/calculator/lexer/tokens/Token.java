package jolyjdia.bot.calculator.lexer.tokens;

import jolyjdia.bot.calculator.Identifiable;
import org.jetbrains.annotations.Contract;

public abstract class Token implements Identifiable {

    private final int position;

    @Contract(pure = true)
    protected Token(int position) {
        this.position = position;
    }

    @Contract(pure = true)
    @Override
    public final int getPosition() {
        return position;
    }

}
