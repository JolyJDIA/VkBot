package jolyjdia.bot.calculator.runtime;

import jolyjdia.bot.calculator.Expression;
import org.jetbrains.annotations.Contract;

public class Constant extends Node {
    private final double value;

    public Constant(double value) {
        super(-1);
        this.value = value;
    }

    @Contract(pure = true)
    @Override
    public final double getValue() {
        return value;
    }

    @Override
    @Contract(pure = true)
    public final RValue optimize() {
        return this;
    }

    @Override
    @Contract(pure = true)
    public final RValue bindVariables(Expression expression) {
        return this;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'c';
    }

}
