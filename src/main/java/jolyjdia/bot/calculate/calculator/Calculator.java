package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.NonNls;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Calculator {
    private static final Pattern COMPILE = Pattern.compile(" ");
    @NonNls private final String userInput;
    private ArrayList<String> formattedUserInput;

    public Calculator(String userInput) {
        this.userInput = COMPILE.matcher(userInput).replaceAll("");
        this.formatUserInput();
    }
    private final void formatUserInput() {
        formattedUserInput = new Parser().parse(userInput);
        System.out.println("До: "+formattedUserInput);
    }

    private final BigDecimal condenseExpression(String operator, int indexVal) {
        BigDecimal x = BigDecimal.ZERO;
        BigDecimal y = BigDecimal.ZERO;
        try {
            x = new BigDecimal(formattedUserInput.get(indexVal - 1));
            y = new BigDecimal(formattedUserInput.get(indexVal + 1));
        } catch (NumberFormatException ignored) {}

        BigDecimal output;
        switch (operator) {
            case "^":
                output = x.pow(y.intValue());
                break;
            case "/":
                if (y.equals(BigDecimal.ZERO)) {
                    return BigDecimal.ZERO;
                } else {
                    output = x.divide(y, MathContext.DECIMAL128);
                }
                break;
            case "*":
                output = x.multiply(y);
                break;
            case "-":
                output = x.subtract(y);
                break;
            case "+":
                output = x.add(y);
                break;
            default:
                return BigDecimal.ZERO;
        }
        return output;
    }

    public final String solveExpression() {
        new ConvertConstants(formattedUserInput).convert();
        formattedUserInput = new MathFunctions(formattedUserInput).evaluateFunctions();

        BigDecimal condense = BigDecimal.ZERO;
        try {
            condense = new BigDecimal(formattedUserInput.get(0));
        } catch (NumberFormatException ignored) {}
        for (int i = 0; i < formattedUserInput.size()-1; ++i) {
            if (formattedUserInput.get(i).equals("-(")) {
                formattedUserInput.add(i, "-1");
                formattedUserInput.add(i+1, "*");
                formattedUserInput.set(i+2, "(");
                i += 2;
            }

            if (formattedUserInput.get(i).equals("(")) {
                EvaluateParentheses evalParenths = new EvaluateParentheses(formattedUserInput);
                evalParenths.condense(i);
                formattedUserInput = evalParenths.getFormattedUserInput();
                i = 0;
            }
        }
        for (int i = 1; i < formattedUserInput.size(); ++i) {
            if (formattedUserInput.get(i).equals("^")) {
                condense = condenseExpression("^", i);

                formattedUserInput.remove(i + 1); // Remove number before operation
                formattedUserInput.remove(i); // Remove operation
                formattedUserInput.set(i - 1, String.valueOf(condense)); // Change number after operation to condensed form

                i = 0;
            }
        }
        for (int i = 1; i < formattedUserInput.size(); ++i) {
            if (formattedUserInput.get(i).equals("*") || formattedUserInput.get(i).equals("/")) {
                if (formattedUserInput.get(i).equals("*")) {
                    condense = condenseExpression("*", i);
                }
                if (formattedUserInput.get(i).equals("/")) {
                    condense = condenseExpression("/", i);
                }
                formattedUserInput.remove(i + 1); // remove number before operation
                formattedUserInput.remove(i); // remove operation
                formattedUserInput.set(i - 1, String.valueOf(condense)); // change number after operation to condensed form
                i = 0;
            }
        }
        for (int i = 1; i < formattedUserInput.size(); ++i) {
            if (formattedUserInput.get(i).equals("+") || formattedUserInput.get(i).equals("-")) {
                if (formattedUserInput.get(i).equals("+")) {
                    condense = condenseExpression("+", i);
                }
                if (formattedUserInput.get(i).equals("-")) {
                    condense = condenseExpression("-", i);
                }
                formattedUserInput.remove(i + 1); // remove number before operation
                formattedUserInput.remove(i); // remove operation
                formattedUserInput.set(i - 1, String.valueOf(condense)); // change number after operation to condensed form

                i = 0;
            }
        }
        return formattedUserInput.size() == 1 ? formattedUserInput.get(0) : "";
    }
}