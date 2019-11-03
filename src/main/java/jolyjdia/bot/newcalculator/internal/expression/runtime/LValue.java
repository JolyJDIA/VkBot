package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.parser.ParserException;

public interface LValue extends RValue {

    double assign(double value);

    @Override
    LValue optimize();

    @Override
    LValue bindVariables(Expression expression) throws ParserException;

}
