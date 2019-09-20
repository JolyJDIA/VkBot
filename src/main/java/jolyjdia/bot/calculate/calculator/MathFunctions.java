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
            "fact",
            "arcsin",
            "arccos",
            "arctan"
    };
    private final ArrayList<String> formattedUserInput;

    @Contract(pure = true)
    MathFunctions(ArrayList<String> formattedUserInput) {
        this.formattedUserInput = formattedUserInput;
    }

    @Contract(pure = true)
    private static double factorialOf(double x) {
        if(x > 100) {
            return -1;
        }
        double factorial = 1;
        for (; x > 1; x--) {
            factorial *= x;
        }
        return factorial;
    }

    private final void formatFunctions() {
        for (@NonNls String operator : ADV_OPERATOR_LIST) {
            for (int i = 0; i < formattedUserInput.size(); i++) {
                if (formattedUserInput.get(i).equals(operator + '(')) {
                    formattedUserInput.set(i, operator);
                    formattedUserInput.add(i + 1, "(");
                }

                if (formattedUserInput.get(i).equals('-' + operator + '(')) {
                    formattedUserInput.set(i, operator);
                    formattedUserInput.add(i, "-1");
                    formattedUserInput.add(i+1, "*");
                    formattedUserInput.add(i + 3, "(");
                }
            }
        }
    }

    final ArrayList<String> evaluateFunctions() {
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
                    case "sqrt":
                        formattedUserInput.set(i, String.valueOf(Math.sqrt(x)));
                        break;
                    case "sin":
                        formattedUserInput.set(i, String.valueOf(Math.sin(x)));
                        break;
                    case "cos":
                        formattedUserInput.set(i, String.valueOf(Math.cos(x)));
                        break;
                    case "tan":
                        formattedUserInput.set(i, String.valueOf(Math.tan(x)));
                        break;
                    case "ln":
                        formattedUserInput.set(i, String.valueOf(Math.log(x)));
                        break;
                    case "abs":
                        formattedUserInput.set(i, String.valueOf(Math.abs(x)));
                        break;
                    case "exp":
                        formattedUserInput.set(i, String.valueOf(Math.exp(x)));
                        break;
                    case "fact":
                        formattedUserInput.set(i, String.valueOf(factorialOf(x)));
                        break;
                    case "arcsin":
                        formattedUserInput.set(i, String.valueOf(Math.asin(x)));
                        break;
                    case "arccos":
                        formattedUserInput.set(i, String.valueOf(Math.acos(x)));
                        break;
                    case "arctan":
                        formattedUserInput.set(i, String.valueOf(Math.atan(x)));
                }
                formattedUserInput.remove(i + 1);
            }
        }

        return formattedUserInput;
    }
}