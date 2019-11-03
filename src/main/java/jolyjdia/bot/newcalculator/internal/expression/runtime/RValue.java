package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.Identifiable;
import jolyjdia.bot.newcalculator.internal.expression.parser.ParserException;

public interface RValue extends Identifiable {

    double getValue();

    RValue optimize();

    RValue bindVariables(Expression expression) throws ParserException;

}
