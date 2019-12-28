package jolyjdia.bot.calculator.parser;

import jolyjdia.bot.calculator.Expression;
import jolyjdia.bot.calculator.ExpressionException;
import jolyjdia.bot.calculator.runtime.LValue;
import jolyjdia.bot.calculator.runtime.RValue;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnboundVariable extends PseudoToken implements LValue {
    @NonNls private final String name;

    UnboundVariable(int position, String name) {
        super(position);
        this.name = name;
    }

    @Override
    public final char id() {
        return 'V';
    }

    @Override
    public final double getValue() {
        return 0;
    }

    @Override
    public final @Nullable LValue optimize() {
        return null;
    }

    @Override
    public final double assign(double value) {
        return 0;
    }

    @Override
    public final @NotNull LValue bindVariables(@NotNull Expression expression) throws ExpressionException {
        final RValue variable = Expression.getVariable(name);
        if (variable == null) {
            throw new ExpressionException(getPosition(), "Переменная " + name + " не найдена");
        }
        return (LValue) variable;
    }

}
