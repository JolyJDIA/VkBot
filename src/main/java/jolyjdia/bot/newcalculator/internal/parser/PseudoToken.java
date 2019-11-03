package jolyjdia.bot.newcalculator.internal.parser;

import jolyjdia.bot.newcalculator.internal.Identifiable;
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
