package jolyjdia.bot.calculate.token;

import jolyjdia.bot.calculate.basic.Computable;
import org.jetbrains.annotations.Contract;

public class TokenComputable extends Token implements Computable {
    private final int precedence;
    private final int args;

    TokenComputable(String notation, int args, int precedence) {
        super(notation);
        this.args = args;
        this.precedence = precedence;
    }

    @Contract(pure = true)
    public final int getArguments() {
        return args;
    }

    @Contract(pure = true)
    public final int getPrecedence() {
        return precedence;
    }

    @Override
    public double compute(double[] params) {
        return 0;
    }

    @Override
    public boolean noInfix() {
        return true;
    }
}
