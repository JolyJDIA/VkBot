package jolyjdia.bot.newcalculator.internal.runtime;

import jolyjdia.bot.newcalculator.internal.Expression;
import jolyjdia.bot.newcalculator.internal.ExpressionException;

public interface LValue extends RValue {

    double assign(double value);

    @Override
    LValue optimize();

    @Override
    LValue bindVariables(Expression expression) throws ExpressionException;

}
