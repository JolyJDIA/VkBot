package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import jolyjdia.bot.newcalculator.internal.expression.Identifiable;

public interface RValue extends Identifiable {

    double getValue();

    RValue optimize();

    RValue bindVariables(Expression expression) throws ExpressionException;

}
