package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Map;

public class EvaluateParentheses {
    private final ArrayList<String> formattedUserInput;
    private String solvedInnerExpression;

    @Contract(pure = true)
    public EvaluateParentheses(ArrayList<String> formattedUserInput) {
        this.formattedUserInput = formattedUserInput;
    }

    final void condense(int start) {
        Map<Integer, Integer> relatedParentheses = new RelatedParentheses(formattedUserInput).evaluateRelations();

        int end = relatedParentheses.get(start);

        StringBuilder innerExpression = new StringBuilder();

        // Populate innerExpression with all of the elemnts inside the parentheses
        for (int t = start + 1; t < end; t++) {
            innerExpression.append(formattedUserInput.get(t));
        }

        Calculator newExpression = new Calculator(innerExpression.toString());
        solvedInnerExpression = newExpression.solveExpression();

        // Removing elements from the back, to avoid IndexOutOfBounds Errors
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