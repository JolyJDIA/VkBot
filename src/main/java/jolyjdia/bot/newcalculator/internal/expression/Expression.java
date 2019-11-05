package jolyjdia.bot.newcalculator.internal.expression;

import com.google.common.collect.ImmutableMap;
import jolyjdia.bot.newcalculator.internal.expression.lexer.Lexer;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.expression.parser.Parser;
import jolyjdia.bot.newcalculator.internal.expression.runtime.Constant;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public final class Expression {
    private static final ThreadLocal<Stack<Expression>> INSTANCE = new ThreadLocal<>();
    private static final Map<String, RValue> CONSTANTS = ImmutableMap.of(
            "pi", new Constant(Math.PI),
            "e", new Constant(Math.E)
    );

    private final RValue root;

    @Contract("_ -> new")
    public static @NotNull Expression compile(String expression) throws ExpressionException {
        return new Expression(expression);
    }

    private Expression(String expression) throws ExpressionException {
        this(Lexer.tokenize(expression));
    }

    private Expression(List<? extends Token> tokens) throws ExpressionException {
        root = Parser.parse(tokens, this);
    }

    public double evaluate() {
        pushInstance();
        try {
            return root.getValue();
        } finally {
            popInstance();
        }
    }

    public static RValue getVariable(String name) {
        return CONSTANTS.get(name);
    }

    private void pushInstance() {
        Stack<Expression> threadLocalExprStack = INSTANCE.get();
        if (threadLocalExprStack == null) {
            INSTANCE.set(threadLocalExprStack = new Stack<>());
        }

        threadLocalExprStack.push(this);
    }

    private static void popInstance() {
        Stack<Expression> threadLocalExprStack = INSTANCE.get();

        threadLocalExprStack.pop();

        if (threadLocalExprStack.isEmpty()) {
            INSTANCE.set(null);
        }
    }
}
