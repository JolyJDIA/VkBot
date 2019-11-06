package jolyjdia.bot.newcalculator.internal.expression;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jolyjdia.bot.newcalculator.internal.expression.lexer.Lexer;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.expression.parser.Parser;
import jolyjdia.bot.newcalculator.internal.expression.runtime.Constant;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public final class Expression {
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .build());
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
        this.root = Parser.parse(tokens, this);
    }
    public double evaluate() {
        Future<Double> result = SERVICE.submit(root::getValue);
        try {
            return result.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (TimeoutException e) {
            e.printStackTrace();
            result.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static RValue getVariable(String name) {
        return CONSTANTS.get(name);
    }
}
