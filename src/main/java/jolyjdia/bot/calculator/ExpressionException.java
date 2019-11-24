package jolyjdia.bot.calculator;

import org.jetbrains.annotations.Contract;

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

    @Contract(pure = true)
    public final int getPosition() {
        return position;
    }

}
