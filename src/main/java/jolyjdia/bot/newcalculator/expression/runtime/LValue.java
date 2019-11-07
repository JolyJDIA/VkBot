package jolyjdia.bot.newcalculator.expression.runtime;

import jolyjdia.bot.newcalculator.expression.Expression;
import jolyjdia.bot.newcalculator.expression.ExpressionException;

public interface LValue extends RValue {

    double assign(double value);

    @Override
    LValue optimize();

    @Override
    LValue bindVariables(Expression expression) throws ExpressionException;

}
