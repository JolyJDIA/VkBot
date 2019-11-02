package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Constant extends Node {

    private final double value;

    public Constant(double value) {
        super(-1);
        this.value = value;
    }

    @Contract(pure = true)
    @Override
    public double getValue() {
        return value;
    }

    @Override
    @Contract(pure = true)
    public RValue optimize() {
        return this;
    }

    @Override
    @Contract(pure = true)
    public RValue bindVariables(Expression expression) {
        return this;
    }

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        return String.valueOf(value);
    }

    @Contract(pure = true)
    @Override
    public char id() {
        return 'c';
    }

}
