package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Calculator {
    private static final Pattern COMPILE = Pattern.compile(" ");
    //private static final String[] OPERATORS1 = {"+", "-", "*", "/", "^", "(", ")", "<"};
    private static final String OPERATORS = "+-*/^()<";

    @NonNls
    private String userInput;
    private ArrayList<String> formattedUserInput = new ArrayList<>();

    public Calculator(String userInput) {
        this.userInput = COMPILE.matcher(userInput).replaceAll("");
    }

    public String getUserInput() {
        return userInput;
    }

    public final void formatUserInput() {
        /**
         * Gets user input Adds spaces in between the operation and the operatorList
         * Splits each item by spaces, into formattedUserInput
         */
        for (int i = 0; i < OPERATORS.length(); ++i) {
            String j = String.valueOf(OPERATORS.charAt(i));
            switch (j) {
                case "(" -> userInput = userInput.replace(j, j + ' ');
                case ")" -> userInput = userInput.replace(j, ' ' + j);
                default -> userInput = userInput.replace(j, ' ' + j + ' ');
            }
        }

        formattedUserInput = new ArrayList<>(Arrays.asList(userInput.split(" ")));

        for (int i = 0; i < formattedUserInput.size(); i++) {
            if (formattedUserInput.get(i).equals("-")) {
                formattedUserInput.set(i, "+");
                formattedUserInput.set(i+1, '-' + formattedUserInput.get(i+1));
            }
        }

    }

    private final double condenseExpression(String operator, int indexVal) {
        double x = 0;
        double y = 0;
        try {//Mather
            x = Double.parseDouble(formattedUserInput.get(indexVal - 1)); // value of i-1
        } catch (NumberFormatException ignored) {}

        try {
            y = Double.parseDouble(formattedUserInput.get(indexVal + 1)); // value of i+1
        } catch (NumberFormatException ignored) {}

        double output; // final output
        switch (operator) {
            case "^":
                output = Math.pow(x, y);
                break;
            case "/":
                if (y == 0) {
                    System.out.println("Error: Can't divide by a zero");
                    return 0;
                } else {
                    output = x / y;
                }
                break;
            case "*":
                output = x * y;
                break;
            case "-":
                output = x - y;
                break;
            case "+":
                output = x + y;
                break;
            default:
                System.out.println("Error: operation does not exist");
                return 0;
        }
        return output;
    }

    public final String solveExpression() {
        formatUserInput();
        new ConvertConstants(formattedUserInput).convert();
        formattedUserInput = new MathFunctions(formattedUserInput).evaluateFunctions();

        /**
         * Set condense to first element of formattedUserInput, in case the user enters
         * a number, constant, etc. This avoids returning a 0 if the user enters, e.g.
         * sqrt(2)
         */
        double condense = 0;
        try {
            condense = Double.parseDouble(formattedUserInput.get(0));
        } catch (NumberFormatException ignored) {}

        // Perform parentheses before everything
        EvaluateParentheses evalParenths;
        for (int i = 0; i < formattedUserInput.size()-1; i++) {
            if (formattedUserInput.get(i).equals("-(")) {
                formattedUserInput.add(i, "-1");
                formattedUserInput.add(i+1, "*");
                formattedUserInput.set(i+2, "(");
                i += 2;
            }
            if (formattedUserInput.get(i).equals("(")) {
                evalParenths = new EvaluateParentheses(formattedUserInput);
                evalParenths.condense(i);
                formattedUserInput = evalParenths.getFormattedUserInput();
                i = 0;
            }
        }

        // Perform Exponents only after all parentheses have been evaluated
        for (int i = 1; i < formattedUserInput.size(); i++) {
            switch (formattedUserInput.get(i)) {
                case "^" -> {
                    condense = condenseExpression("^", i);
                    formattedUserInput.remove(i + 1); // Remove number before operation
                    formattedUserInput.remove(i); // Remove operation
                    formattedUserInput.set(i - 1, String.valueOf(condense)); // Change number after operation to condensed form
                    i = 0;
                }
                case "*", "/" -> {
                    if (formattedUserInput.get(i).equals("*"))
                        condense = condenseExpression("*", i);
                    if (formattedUserInput.get(i).equals("/"))
                        condense = condenseExpression("/", i);
                    formattedUserInput.remove(i + 1); // remove number before operation
                    formattedUserInput.remove(i); // remove operation
                    formattedUserInput.set(i - 1, String.valueOf(condense)); // change number after operation to condensed form
                    i = 0;
                }
                case "+", "-" -> {
                    if (formattedUserInput.get(i).equals("+"))
                        condense = condenseExpression("+", i);
                    if (formattedUserInput.get(i).equals("-"))
                        condense = condenseExpression("-", i);
                    formattedUserInput.remove(i + 1); // remove number before operation
                    formattedUserInput.remove(i); // remove operation
                    formattedUserInput.set(i - 1, String.valueOf(condense)); // change number after operation to condensed form
                    i = 0;
                }
            }
        }


        return formattedUserInput.size() == 1 ? formattedUserInput.get(0) : "";
    }
}