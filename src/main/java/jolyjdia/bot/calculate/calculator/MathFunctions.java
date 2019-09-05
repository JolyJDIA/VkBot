package jolyjdia.bot.calculate.calculator;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;

public class MathFunctions {
    private static final String[] advOperatorList = {
            "sqrt",
            "sin",
            "cos",
            "tan",
            "ln",
            "abs",
            "exp",
            "fact",
            "!",
            "pow",
            "arcsin",
            "arccos",
            "arctan"};
    private final ArrayList<String> formattedUserInput;

    @Contract(pure = true)
    public MathFunctions(ArrayList<String> formattedUserInput) {
        this.formattedUserInput = formattedUserInput;
    }

    @Contract(pure = true)
    public static double factorialOf(double x) {
        double factorial = 1;
        
        for (int i = (int)x; i > 1; i--) {
            factorial *= i;
        }
        return factorial;
    }

    public final void formatFunctions() {
        /**
         * "2+sin(x)" is represented as ["2", "+", "sin(", "x", ")"] The loop replaces
         * "sin(" with "sin" or "ln(" with "ln" etc.. Then adds a ( after sin. eg. ["2",
         * "+", "sin", "(", "x", ")"]
         */
        for (@NonNls String operator : advOperatorList) {
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
        double x;

        EvaluateParentheses evalPrenths;

        for (String operator : advOperatorList) {
            for (int i = 0; i < formattedUserInput.size(); i++) {

                if (formattedUserInput.get(i).equals(operator)) {
                    // Evaluates [(, x, )] from [sin, (, x, )], leaving us with [sin, x]
                    evalPrenths = new EvaluateParentheses(formattedUserInput);
                    evalPrenths.condense(i + 1); 
                    x = Double.parseDouble(evalPrenths.getSolvedInnerExpression());

                    switch (operator) {
                        case "sqrt" -> formattedUserInput.set(i, String.valueOf(Math.sqrt(x)));
                        case "sin" -> formattedUserInput.set(i, String.valueOf(Math.sin(x)));
                        case "cos" -> formattedUserInput.set(i, String.valueOf(Math.cos(x)));
                        case "tan" -> formattedUserInput.set(i, String.valueOf(Math.tan(x)));
                        case "ln" -> formattedUserInput.set(i, String.valueOf(Math.log(x)));
                        case "abs" -> formattedUserInput.set(i, String.valueOf(Math.abs(x)));
                        case "exp" -> formattedUserInput.set(i, String.valueOf(Math.exp(x)));
                        case "fact" -> formattedUserInput.set(i, String.valueOf(factorialOf(x)));
                        case "pow" -> formattedUserInput.set(i, String.valueOf(Math.pow(x, 2)));
                        case "arcsin" -> formattedUserInput.set(i, String.valueOf(Math.asin(x)));
                        case "arccos" -> formattedUserInput.set(i, String.valueOf(Math.acos(x)));
                        case "arctan" -> formattedUserInput.set(i, String.valueOf(Math.atan(x)));
                    }
                    
                    formattedUserInput.remove(i + 1); // Remove x from formattedUserInput
                }

            }
        }

        return formattedUserInput;
    }
}