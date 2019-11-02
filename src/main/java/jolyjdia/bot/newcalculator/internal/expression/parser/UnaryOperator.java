package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.OperatorToken;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UnaryOperator extends PseudoToken {

    final String operator;

    public UnaryOperator(@NotNull OperatorToken operatorToken) {
        this(operatorToken.getPosition(), operatorToken.operator);
    }

    UnaryOperator(int position, String operator) {
        super(position);
        this.operator = operator;
    }

    @Contract(pure = true)
    @Override
    public final char id() {
        return 'p';
    }

    @Override
    @Contract(pure = true)
    public final @NotNull String toString() {
        return "UnaryOperator(" + operator + ')';
    }

}
