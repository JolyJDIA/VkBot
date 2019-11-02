package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.parser.ParserException;

public interface LValue extends RValue {

    double assign(double value) throws EvaluationException;

    @Override
    LValue optimize() throws EvaluationException;

    @Override
    LValue bindVariables(Expression expression) throws ParserException;

}
