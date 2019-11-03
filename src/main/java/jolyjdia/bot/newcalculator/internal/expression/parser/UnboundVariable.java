package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import jolyjdia.bot.newcalculator.internal.expression.runtime.LValue;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnboundVariable extends PseudoToken implements LValue {
    @NonNls private final String name;

    UnboundVariable(int position, String name) {
        super(position);
        this.name = name;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'V';
    }

    @Contract(pure = true)
    @Override
    public final double getValue() {
        return 0;
    }

    @Override
    @Contract(pure = true)
    public final @Nullable LValue optimize() {
        return null;
    }

    @Contract(pure = true)
    @Override
    public final double assign(double value) {
        return 0;
    }

    @Override
    public final @NotNull LValue bindVariables(@NotNull Expression expression) throws ExpressionException {
        final RValue variable = expression.getVariable(name);
        if (variable == null) {
            throw new ExpressionException(getPosition(), "Переменная " + name + " не найдена");
        }
        return (LValue) variable;
    }

}
