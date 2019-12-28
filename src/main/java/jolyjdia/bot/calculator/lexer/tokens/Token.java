package jolyjdia.bot.calculator.lexer.tokens;

import jolyjdia.bot.calculator.Identifiable;

public abstract class Token implements Identifiable {

    private final int position;

    protected Token(int position) {
        this.position = position;
    }

    @Override
    public final int getPosition() {
        return position;
    }

}
