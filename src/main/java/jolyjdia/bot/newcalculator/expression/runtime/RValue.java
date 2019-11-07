package jolyjdia.bot.newcalculator.expression.runtime;

import jolyjdia.bot.newcalculator.expression.Expression;
import jolyjdia.bot.newcalculator.expression.ExpressionException;
import jolyjdia.bot.newcalculator.expression.Identifiable;

public interface RValue extends Identifiable {

    double getValue();

    RValue optimize();

    RValue bindVariables(Expression expression) throws ExpressionException;

}
