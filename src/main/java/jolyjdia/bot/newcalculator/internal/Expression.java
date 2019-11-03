package jolyjdia.bot.newcalculator.internal;

import com.google.common.collect.ImmutableMap;
import jolyjdia.bot.newcalculator.internal.lexer.Lexer;
import jolyjdia.bot.newcalculator.internal.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.parser.Parser;
import jolyjdia.bot.newcalculator.internal.runtime.Constant;
import jolyjdia.bot.newcalculator.internal.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public final class Expression {

    private static final ThreadLocal<Stack<Expression>> instance = new ThreadLocal<>();

    private final Map<String, RValue> constants = ImmutableMap.of(
            "e", new Constant(Math.E),
            "pi", new Constant(Math.PI)
    );

    private final RValue root;

    @Contract("_ -> new")
    public static @NotNull Expression compile(String expression) throws ExpressionException {
        return new Expression(expression);
    }

    private Expression(String expression) throws ExpressionException {
        this(Lexer.tokenize(expression));
    }

    private Expression(List<Token> tokens) throws ExpressionException {
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

    public RValue getVariable(String name) {
        return constants.get(name);
    }

    private void pushInstance() {
        Stack<Expression> threadLocalExprStack = instance.get();
        if (threadLocalExprStack == null) {
            instance.set(threadLocalExprStack = new Stack<>());
        }

        threadLocalExprStack.push(this);
    }

    private static void popInstance() {
        Stack<Expression> threadLocalExprStack = instance.get();

        threadLocalExprStack.pop();

        if (threadLocalExprStack.isEmpty()) {
            instance.set(null);
        }
    }
}
