package jolyjdia.bot.newcalculator.internal.runtime;

import jolyjdia.bot.newcalculator.internal.Expression;
import jolyjdia.bot.newcalculator.internal.ExpressionException;
import jolyjdia.bot.newcalculator.internal.Identifiable;

public interface RValue extends Identifiable {

    double getValue();

    RValue optimize();

    RValue bindVariables(Expression expression) throws ExpressionException;

}
