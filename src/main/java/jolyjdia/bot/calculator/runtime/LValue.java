package jolyjdia.bot.calculator.runtime;

import jolyjdia.bot.calculator.Expression;
import jolyjdia.bot.calculator.ExpressionException;

public interface LValue extends RValue {

    double assign(double value);

    @Override
    LValue optimize();

    @Override
    LValue bindVariables(Expression expression) throws ExpressionException;

}
