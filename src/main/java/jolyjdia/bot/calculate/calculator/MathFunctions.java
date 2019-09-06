package jolyjdia.bot.calculate.calculator;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;

public class MathFunctions {
    public static final String[] ADV_OPERATOR_LIST = {
            "sqrt",
            "sin",
            "cos",
            "tan",
            "ln",
            "abs",
            "exp",
            "fact"
    };
    private final ArrayList<String> formattedUserInput;

    @Contract(pure = true)
    public MathFunctions(ArrayList<String> formattedUserInput) {
        this.formattedUserInput = formattedUserInput;
    }

    @Contract(pure = true)
    public static double factorialOf(double x) {
        double factorial = 1;
        
        for (; x > 1; x--) {
            factorial *= x;
        }
        return factorial;
    }

    public final void formatFunctions() {
        /**
         * "2+sin(x)" is represented as ["2", "+", "sin(", "x", ")"] The loop replaces
         * "sin(" with "sin" or "ln(" with "ln" etc.. Then adds a ( after sin. eg. ["2",
         * "+", "sin", "(", "x", ")"]
         */
        for (@NonNls String operator : ADV_OPERATOR_LIST) {
            for (int i = 0; i < formattedUserInput.size(); i++) {
                if (formattedUserInput.get(i).equals(operator + '(')) {
                    formattedUserInput.set(i, operator);
                    formattedUserInput.add(i + 1, "(");
                }

                // Negative functions
                if (formattedUserInput.get(i).equals('-' + operator + '(')) {
                    formattedUserInput.set(i, operator);
                    formattedUserInput.add(i, "-1");
                    formattedUserInput.add(i+1, "*");
                    formattedUserInput.add(i + 3, "(");
                }
            }
        }
    }

    public final ArrayList<String> evaluateFunctions() {
        formatFunctions();
        for (String operator : ADV_OPERATOR_LIST) {
            for (int i = 0; i < formattedUserInput.size(); i++) {
                if (!formattedUserInput.get(i).equals(operator)) {
                    continue;
                }
                EvaluateParentheses evalPrenths = new EvaluateParentheses(formattedUserInput);
                evalPrenths.condense(i + 1);
                double x = Double.parseDouble(evalPrenths.getSolvedInnerExpression());

                switch (operator) {
                    case "sqrt" -> formattedUserInput.set(i, String.valueOf(Math.sqrt(x)));
                    case "sin" -> formattedUserInput.set(i, String.valueOf(Math.sin(x)));
                    case "cos" -> formattedUserInput.set(i, String.valueOf(Math.cos(x)));
                    case "tan" -> formattedUserInput.set(i, String.valueOf(Math.tan(x)));
                    case "ln" -> formattedUserInput.set(i, String.valueOf(Math.log(x)));
                    case "abs" -> formattedUserInput.set(i, String.valueOf(Math.abs(x)));
                    case "exp" -> formattedUserInput.set(i, String.valueOf(Math.exp(x)));
                    case "fact" -> formattedUserInput.set(i, String.valueOf(factorialOf(x)));
                }
                formattedUserInput.remove(i + 1);
            }
        }

        return formattedUserInput;
    }
}