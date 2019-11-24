package jolyjdia.bot.calculator.parser;

import jolyjdia.bot.calculator.Identifiable;
import org.jetbrains.annotations.Contract;

public abstract class PseudoToken implements Identifiable {
    private final int position;

    @Contract(pure = true)
    PseudoToken(int position) {
        this.position = position;
    }

    @Contract(pure = true)
    @Override
    public final int getPosition() {
        return position;
    }

}
