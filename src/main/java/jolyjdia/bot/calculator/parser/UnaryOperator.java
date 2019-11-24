package jolyjdia.bot.calculator.parser;

import jolyjdia.bot.calculator.lexer.tokens.OperatorToken;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UnaryOperator extends PseudoToken {
    final String operator;

    UnaryOperator(@NotNull OperatorToken operatorToken) {
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

}
