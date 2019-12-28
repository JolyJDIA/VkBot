package jolyjdia.bot.calculator.parser;

import jolyjdia.bot.calculator.Identifiable;

public abstract class PseudoToken implements Identifiable {
    private final int position;

    PseudoToken(int position) {
        this.position = position;
    }

    @Override
    public final int getPosition() {
        return position;
    }

}
