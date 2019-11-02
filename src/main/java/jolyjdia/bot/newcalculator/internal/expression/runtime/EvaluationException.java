package jolyjdia.bot.newcalculator.internal.expression.runtime;

import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class EvaluationException extends ExpressionException {

    private static final long serialVersionUID = -7604535507128854105L;

    public EvaluationException(int position) {
        super(position, getPrefix(position));
    }

    public EvaluationException(int position, @NonNls String message, Throwable cause) {
        super(position, getPrefix(position) + ": " + message, cause);
    }

    public EvaluationException(int position, @NonNls String message) {
        super(position, getPrefix(position) + ": " + message);
    }

    public EvaluationException(int position, Throwable cause) {
        super(position, getPrefix(position), cause);
    }

    @NonNls
    @Contract(pure = true)
    private static @NotNull String getPrefix(int position) {
        return position < 0 ? "Evaluation error" : ("Evaluation error at " + (position + 1));
    }

}
