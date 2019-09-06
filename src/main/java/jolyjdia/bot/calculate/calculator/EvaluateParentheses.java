package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class EvaluateParentheses {
    private final ArrayList<String> formattedUserInput;
    private String solvedInnerExpression;

    @Contract(pure = true)
    public EvaluateParentheses(ArrayList<String> formattedUserInput) {
        this.formattedUserInput = formattedUserInput;
    }

    final void condense(int start) {
        int end = new RelatedParentheses(formattedUserInput).evaluateRelations().get(start);

        StringBuilder innerExpression = new StringBuilder();
        for (int t = start + 1; t < end; t++) {
            innerExpression.append(formattedUserInput.get(t));
        }

        Calculator newExpression = new Calculator(innerExpression.toString());
        this.solvedInnerExpression = newExpression.solveExpression();

        if (end >= start + 1) {
            formattedUserInput.subList(start + 1, end + 1).clear();
        }
        formattedUserInput.set(start, solvedInnerExpression);
    }

    @Contract(pure = true)
    public final ArrayList<String> getFormattedUserInput() {
        return formattedUserInput;
    }

    @Contract(pure = true)
    public final String getSolvedInnerExpression() {
        return solvedInnerExpression;
    }
}