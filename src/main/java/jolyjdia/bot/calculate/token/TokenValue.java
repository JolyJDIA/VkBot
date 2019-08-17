package jolyjdia.bot.calculate.token;

public class TokenValue extends Token {
    private final double value;

    public TokenValue(double value) {
        super(String.valueOf(value));
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
