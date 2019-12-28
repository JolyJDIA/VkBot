package jolyjdia.bot.calculator;

public class ExpressionException extends Exception {

    private static final long serialVersionUID = -6656866842440051717L;
    private final int position;

    public ExpressionException(int position, String message, Throwable cause) {
        super(message, cause);
        this.position = position;
    }

    public ExpressionException(int position, String message) {
        super(message);
        this.position = position;
    }

    public final int getPosition() {
        return position;
    }

}
