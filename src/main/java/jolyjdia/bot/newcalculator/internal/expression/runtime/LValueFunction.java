package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.parser.ParserException;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Method;

public class LValueFunction extends Function implements LValue {

    private final Object[] setterArgs;
    private final Method setter;

    LValueFunction(int position, Method getter, Method setter, RValue... args) {
        super(position, getter, args);
        assert (getter.isAnnotationPresent(Dynamic.class));

        setterArgs = new Object[args.length + 1];
        System.arraycopy(args, 0, setterArgs, 0, args.length);
        this.setter = setter;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'l';
    }

    @Override
    public final double assign(double value) throws EvaluationException {
        setterArgs[setterArgs.length - 1] = value;
        return invokeMethod(setter, setterArgs);
    }

    @Override
    public final LValue optimize() throws EvaluationException {
        final RValue optimized = super.optimize();
        if (optimized == this) {
            return this;
        }

        if (optimized instanceof Function) {
            return new LValueFunction(optimized.getPosition(), method, setter, ((Function) optimized).args);
        }

        return (LValue) optimized;
    }

    @Contract("_ -> this")
    @Override
    public final LValue bindVariables(Expression expression) throws ParserException {
        super.bindVariables(expression);
        return this;
    }
}
