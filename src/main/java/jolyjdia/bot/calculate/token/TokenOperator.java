package jolyjdia.bot.calculate.token;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TokenOperator extends TokenComputable {

    private TokenOperator.EnumOperator type;

    public TokenOperator(@NotNull TokenOperator.EnumOperator type) {
        this(type.notation, type.precedence);
        this.type = type;
    }

    public TokenOperator(String notation, int precedence) {
        super(notation, 2, precedence);
    }

    @Contract(pure = true)
    @Override
    public final boolean noInfix() {
        return false;
    }

    @Contract(pure = true)
    @Override
    public final double compute(double[] params) {
        double a = params[0];
        double b = params[1];
        switch (type) {
            case ADD:
                return a + b;
            case SUBTRACT:
                return a - b;
            case MULTIPLY:
                return a * b;
            case DIVIDE:
                return a / b;
            case POWER:
                return Math.pow(a, b);
        }
        return a;
    }

    public enum EnumOperator {
        ADD("+", 0),
        SUBTRACT("-", 0),
        MULTIPLY("*", 1),
        DIVIDE("/", 1),
        POWER("^", 2);
        public String notation;
        public int precedence;

        @Contract(pure = true)
        EnumOperator(String notation, int precendence) {
            this.notation = notation;
            this.precedence = precendence;
        }
    }
}
