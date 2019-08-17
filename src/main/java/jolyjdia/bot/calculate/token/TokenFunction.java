package jolyjdia.bot.calculate.token;


public class TokenFunction extends TokenComputable {
    public TokenFunction(String notation, int args) {
        super(notation, args, Integer.MAX_VALUE);
    }

    @Override
    public double compute(double[] params) {
        return 0;
    }

    @Override
    public boolean noInfix() {
        return true;
    }

    @Override
    public String toString() {
        return getNotation();
    }
}
