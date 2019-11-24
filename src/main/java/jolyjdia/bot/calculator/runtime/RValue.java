package jolyjdia.bot.calculator.runtime;

import jolyjdia.bot.calculator.Expression;
import jolyjdia.bot.calculator.ExpressionException;
import jolyjdia.bot.calculator.Identifiable;

public interface RValue extends Identifiable {

    double getValue();

    RValue optimize();

    RValue bindVariables(Expression expression) throws ExpressionException;

}
