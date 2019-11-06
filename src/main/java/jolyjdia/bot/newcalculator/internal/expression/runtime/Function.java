package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Function extends Node {
    private final Method method;
    public final RValue[] args;

    Function(int position, Method method, RValue... args) {
        super(position);
        this.method = method;
        this.args = args;
    }

    @Override
    public final double getValue() {
        return invokeMethod(method, args);
    }

    private static double invokeMethod(@NotNull Method method, Object[] args) {
        try {
            return (Double) method.invoke(null, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'f';
    }

    @Override
    public final @NotNull RValue optimize() {
        final RValue[] optimizedArgs = new RValue[args.length];
        boolean optimizable = true;
        int position = getPosition();
        for (int i = 0; i < args.length; ++i) {
            final RValue optimized = optimizedArgs[i] = args[i].optimize();

            if (!(optimized instanceof Constant)) {
                optimizable = false;
            }

            if (optimized.getPosition() < position) {
                position = optimized.getPosition();
            }
        }

        return optimizable ?
                new Constant(invokeMethod(method, optimizedArgs)) :
                new Function(position, method, optimizedArgs);
    }

    @Contract("_ -> this")
    @Override
    public final RValue bindVariables(Expression expression) throws ExpressionException {
        for (int i = 0; i < args.length; ++i) {
            args[i] = args[i].bindVariables(expression);
        }
        return this;
    }

}
