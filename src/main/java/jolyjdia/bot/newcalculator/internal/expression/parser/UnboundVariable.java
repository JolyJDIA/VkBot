package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.runtime.EvaluationException;
import jolyjdia.bot.newcalculator.internal.expression.runtime.LValue;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class UnboundVariable extends PseudoToken implements LValue {
    @NonNls
    private final String name;

    UnboundVariable(int position, String name) {
        super(position);
        this.name = name;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'V';
    }

    @Contract(" -> fail")
    @Override
    public final double getValue() throws EvaluationException {
        throw new EvaluationException(getPosition(), "Tried to evaluate unbound variable!");
    }

    @Contract(" -> fail")
    @Override
    public final LValue optimize() throws EvaluationException {
        throw new EvaluationException(getPosition(), "Tried to optimize unbound variable!");
    }

    @Contract("_ -> fail")
    @Override
    public final double assign(double value) throws EvaluationException {
        throw new EvaluationException(getPosition(), "Tried to assign unbound variable!");
    }

    @Override
    public final @NotNull LValue bindVariables(@NotNull Expression expression) throws ParserException {
        final RValue variable = expression.getVariable(name);
        if (variable == null) {
            throw new ParserException(getPosition(), "Variable '" + name + "' not found");
        }

        return (LValue) variable;
    }

}
