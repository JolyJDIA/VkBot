package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class EvaluateParentheses {
    private final ArrayList<String> formattedUserInput;
    private String solvedInnerExpression;

    @Contract(pure = true)
    public EvaluateParentheses(ArrayList<String> formattedUserInput) {
        this.formattedUserInput = formattedUserInput;
    }

    final void condense(int start) {

        int end = evaluateRelations().get(start);

        StringBuilder innerExpression = new StringBuilder();
        for (int t = start + 1; t < end; t++) {
            innerExpression.append(formattedUserInput.get(t));
        }

        Calculate newExpression = new Calculate(innerExpression.toString());
        this.solvedInnerExpression = newExpression.solveExpression();

        if (end >= start + 1) {
            formattedUserInput.subList(start + 1, end + 1).clear();
        }
        formattedUserInput.set(start, solvedInnerExpression);
    }
    public final @NotNull Map<Integer, Integer> evaluateRelations() {
        ArrayList<Integer> openingParenthesis = new ArrayList<>();
        Map<Integer, Integer> relationships = new TreeMap<>();

        for (int i = 0; i < formattedUserInput.size(); i++) {
            if (formattedUserInput.get(i).equals("(")) {
                openingParenthesis.add(i);
            } else if (formattedUserInput.get(i).equals(")") && !openingParenthesis.isEmpty()) {
                relationships.put(openingParenthesis.get( openingParenthesis.size()-1 ), i);
                openingParenthesis.remove(openingParenthesis.size()-1);
            }
        }
        return relationships;
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