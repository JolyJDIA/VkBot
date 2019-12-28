package jolyjdia.bot.calculator;

import com.google.common.collect.ImmutableMap;
import jolyjdia.bot.calculator.lexer.Lexer;
import jolyjdia.bot.calculator.lexer.tokens.Token;
import jolyjdia.bot.calculator.parser.Parser;
import jolyjdia.bot.calculator.runtime.Constant;
import jolyjdia.bot.calculator.runtime.RValue;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public final class Expression {
    private static final Map<String, RValue> CONSTANTS = ImmutableMap.of(
            "pi", new Constant(Math.PI),
            "e", new Constant(Math.E)
    );
    private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.#####");//static

    private final RValue root;

    public static @NotNull Expression compile(String expression) throws ExpressionException {
        return new Expression(expression);
    }

    private Expression(String expression) throws ExpressionException {
        this(Lexer.tokenize(expression));
    }

    private Expression(List<? extends Token> tokens) throws ExpressionException {
        this.root = Parser.parse(tokens, this);
    }
    public String evaluate() {
        double reply = root.getValue();
        return Double.isNaN(reply) ? "NaN" : DECIMAL_FORMAT.format(reply);
    }

    public static RValue getVariable(String name) {
        return CONSTANTS.get(name);
    }
}
