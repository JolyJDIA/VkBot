package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sequence extends Node {
    private final RValue[] sequence;

    public Sequence(int position, RValue... sequence) {
        super(position);
        this.sequence = sequence;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 's';
    }

    @Override
    public final double getValue() {
        double ret = 0;
        for (RValue invokable : sequence) {
            ret = invokable.getValue();
        }
        return ret;
    }

    @Override
    public final RValue optimize() {
        final List<RValue> newSequence = new ArrayList<>();

        @Nullable RValue droppedLast = null;
        for (RValue invokable : sequence) {
            droppedLast = null;
            invokable = invokable.optimize();
            if (invokable instanceof Sequence) {
                Collections.addAll(newSequence, ((Sequence) invokable).sequence);
            } else if (invokable instanceof Constant) {
                droppedLast = invokable;
            } else {
                newSequence.add(invokable);
            }
        }

        if (droppedLast != null) {
            newSequence.add(droppedLast);
        }

        if (newSequence.size() == 1) {
            return newSequence.get(0);
        }

        return new Sequence(getPosition(), newSequence.toArray(new RValue[0]));
    }

    @Contract("_, _ -> this")
    @Override
    public final RValue bindVariables(Expression expression) throws ExpressionException {
        for (int i = 0; i < sequence.length; ++i) {
            sequence[i] = sequence[i].bindVariables(expression);
        }

        return this;
    }

}
