package jolyjdia.bot.calculator.runtime;

import jolyjdia.bot.calculator.Expression;

public class Constant extends Node {
    private final double value;

    public Constant(double value) {
        super(-1);
        this.value = value;
    }

    @Override
    public final double getValue() {
        return value;
    }

    @Override
    public final RValue optimize() {
        return this;
    }

    @Override
    public final RValue bindVariables(Expression expression) {
        return this;
    }

    @Override
    public final char id() {
        return 'c';
    }

}
