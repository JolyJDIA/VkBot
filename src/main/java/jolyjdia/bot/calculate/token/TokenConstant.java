package jolyjdia.bot.calculate.token;

public class TokenConstant extends TokenValue {
    public TokenConstant(String notation, double value) {
        super(value);
        this.setNotation(notation);
    }
}
