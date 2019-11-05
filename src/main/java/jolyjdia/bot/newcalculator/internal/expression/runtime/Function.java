package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Function extends Node {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Dynamic { }

    final Method method;
    final RValue[] args;

    Function(int position, Method method, RValue... args) {
        super(position);
        this.method = method;
        this.args = args;
    }

    @Override
    public final double getValue() {
        return invokeMethod(method, args);
    }

    static double invokeMethod(@NotNull Method method, Object[] args) {
        try {
            return (Double) method.invoke(null, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public char id() {
        return 'f';
    }

    @Override
    public RValue optimize() {
        final RValue[] optimizedArgs = new RValue[args.length];
        boolean optimizable = !method.isAnnotationPresent(Dynamic.class);
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

    @Override
    public RValue bindVariables(Expression expression) throws ExpressionException {
        for (int i = 0; i < args.length; ++i) {
            args[i] = args[i].bindVariables(expression);
        }
        return this;
    }

}
