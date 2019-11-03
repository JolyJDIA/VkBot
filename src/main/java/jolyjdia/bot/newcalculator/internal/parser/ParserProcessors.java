package jolyjdia.bot.newcalculator.internal.parser;

import com.google.common.collect.ImmutableMap;
import jolyjdia.bot.newcalculator.internal.ExpressionException;
import jolyjdia.bot.newcalculator.internal.Identifiable;
import jolyjdia.bot.newcalculator.internal.lexer.tokens.OperatorToken;
import jolyjdia.bot.newcalculator.internal.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.runtime.Operators;
import jolyjdia.bot.newcalculator.internal.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

final class ParserProcessors {
    private static final Map<String, String> UNARY_OP = ImmutableMap.<String, String>builder()
            .put("-", "neg")
            .put("~", "inv")
            .put("x!", "fac")
            .build();

    private static final ImmutableMap<String, String>[] BINARY_OP = new ImmutableMap[3];
    static {
        BINARY_OP[0] = ImmutableMap.of(
                "%", "mod",
                "*", "mul",
                "/", "div"
        );
        BINARY_OP[1] = ImmutableMap.of(
                "+", "add",
                "-", "sub",
                "^", "pow"
        );
    }

    @Contract(pure = true)
    private ParserProcessors() {}

    static @NotNull RValue processExpression(LinkedList<Identifiable> input) throws ExpressionException {
        return Objects.requireNonNull(processBinaryOpsLA(input, 1));
    }
    private static @Nullable RValue processBinaryOpsLA(@NotNull LinkedList<Identifiable> input, int level) throws ExpressionException {
        if (level < 0) {
            return processUnaryOps(input);
        }
        LinkedList<Identifiable> lhs = new LinkedList<>();
        LinkedList<Identifiable> rhs = new LinkedList<>();
        String operator = null;
        Iterator<Identifiable> iterator = input.descendingIterator();
        while (iterator.hasNext()) {
            Identifiable identifiable = iterator.next();
            if (operator == null) {
                rhs.addFirst(identifiable);

                if (!(identifiable instanceof OperatorToken)) {
                    continue;
                }

                operator = BINARY_OP[level].get(((OperatorToken) identifiable).operator);
                if (operator == null) {
                    continue;
                }

                rhs.removeFirst();
            } else {
                lhs.addFirst(identifiable);
            }
        }

        RValue rhsInvokable = processBinaryOpsLA(rhs, level - 1);
        if (operator == null) {
            return rhsInvokable;
        }

        RValue lhsInvokable = processBinaryOpsLA(lhs, level);
        try {
            return Operators.getOperator(input.get(0).getPosition(), operator, lhsInvokable, rhsInvokable);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    private static @Nullable RValue processUnaryOps(@NotNull LinkedList<Identifiable> input) throws ExpressionException {
        @NonNls final Identifiable center;
        Deque<UnaryOperator> postfixes = new LinkedList<>();
        do {
            if (input.isEmpty()) {
                throw new ExpressionException(-1, "Выражение отсутствует");
            }

            final Identifiable last = input.removeLast();
            if (last instanceof OperatorToken) {
                postfixes.addLast(new UnaryOperator(last.getPosition(), 'x' + ((OperatorToken) last).operator));
            } else if (last instanceof UnaryOperator) {
                postfixes.addLast(new UnaryOperator(last.getPosition(), 'x' + ((UnaryOperator) last).operator));
            } else {
                center = last;
                break;
            }
        } while (true);

        if (!(center instanceof RValue)) {
            throw new ExpressionException(center.getPosition(), "Ожидаемое выражение, найдено " + center);
        }

        input.addAll(postfixes);

        RValue ret = (RValue) center;
        while (!input.isEmpty()) {
            @NonNls final Identifiable last = input.removeLast();
            final int lastPosition = last.getPosition();
            if (last instanceof UnaryOperator) {
                final String operator = ((UnaryOperator) last).operator;
                if (operator.equals("+")) {
                    continue;
                }

                String opName = UNARY_OP.get(operator);
                if (opName != null) {
                    try {
                        ret = Operators.getOperator(lastPosition, opName, ret);
                        continue;
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                }
            }

            if (last instanceof Token) {
                throw new ExpressionException(lastPosition, "Нет такого токена: " + last);
            } else if (last instanceof RValue) {
                throw new ExpressionException(lastPosition, "Нет такого выражения: " + last);
            } else {
                throw new ExpressionException(lastPosition, "Нет такого элемента: " + last);
            }
        }
        return ret;
    }

}
