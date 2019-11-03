package jolyjdia.bot.newcalculator.internal.expression.parser;

import com.google.common.collect.ImmutableMap;
import jolyjdia.bot.newcalculator.internal.expression.Identifiable;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.OperatorToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.expression.runtime.Operators;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

final class ParserProcessors {
    private static final Map<String, String> UNARY_OP_MAP = ImmutableMap.<String, String>builder()
            .put("-", "neg")
            .put("!", "not")
            .put("~", "inv")
            .put("x!", "fac")
            .build();
    /**private static final Map<String, String> BINARY_OP = ImmutableMap.<String, String>builder()
            .put("-", "sub")
            .put("+", "add")
            .put("%", "mod")
            .put("/", "div")
            .put("*", "mul")
            .put("**", "pow")
            .put("^", "pow")
            .build();*/
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

    static @NotNull RValue processExpression(LinkedList<Identifiable> input) throws ParserException {
        return Objects.requireNonNull(processBinaryOpsLA(input, 1));
    }
    private static @Nullable RValue processBinaryOpsLA(@NotNull LinkedList<Identifiable> input, int level) throws ParserException {
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
                System.out.println(operator);
                if (operator == null) {
                    continue;
                }

                rhs.removeFirst();
            } else {
                lhs.addFirst(identifiable);
            }
        }

        RValue rhsInvokable = processBinaryOpsLA(rhs, level -1);
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
    private static @Nullable RValue processUnaryOps(@NotNull LinkedList<Identifiable> input) throws ParserException {
        final Identifiable center;
        Deque<UnaryOperator> postfixes = new LinkedList<>();
        do {
            if (input.isEmpty()) {
                throw new ParserException(-1, "Expression missing.");
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
            throw new ParserException(center.getPosition(), "Expected expression, found " + center);
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

                String opName = UNARY_OP_MAP.get(operator);
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
                throw new ParserException(lastPosition, "Extra token found in expression: " + last);
            } else if (last instanceof RValue) {
                throw new ParserException(lastPosition, "Extra expression found: " + last);
            } else {
                throw new ParserException(lastPosition, "Extra element found: " + last);
            }
        }
        return ret;
    }

}
