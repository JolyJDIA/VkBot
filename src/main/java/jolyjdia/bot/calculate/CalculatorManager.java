package jolyjdia.bot.calculate;

import api.Bot;
import api.utils.KeyboardUtils;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

final class CalculatorManager {
    static final Pattern MATH = Pattern.compile("[a-zA-Z.\\d+\\-*/()^ ]*");
    private static final Map<Integer, String> history = new HashMap<>();
    @Contract(pure = true)
    private CalculatorManager() {}

    static void actionsCalculator(int peerId, @NotNull @NonNls String element) {
        switch (element) {
            case "=":
                Calculator calculator = new Calculator(history.get(peerId));
                String answer = calculator.solveExpression();
                if (answer.isEmpty()) {
                    return;
                }
                closeCalculatorBoard(answer, peerId);
                break;
            case "C":
                history.put(peerId, "");
                break;
            case "<=":
                String get = history.get(peerId);
                String set = get.substring(0, get.length() - 1);

                history.put(peerId, set);
                if (set.isEmpty()) {
                    return;
                }
                Bot.sendMessage(set, peerId);
                break;
            default:
                if (MATH.matcher(element).matches()) {
                    history.compute(peerId, (k, v) -> v + element);
                    Bot.sendMessage(history.get(peerId), peerId);
                } else {
                    closeCalculatorBoard("Я вижу, дружок, тебе не нужен калькулятор", peerId);
            }
        }
    }
    static void closeCalculatorBoard(String text, int peerId) {
        history.remove(peerId);
        Bot.sendKeyboard(text, peerId, KeyboardUtils.EMPTY_KEYBOARD);
    }
    static void openCalculatorBoard(int peerId) {
        history.put(peerId, "");
        Bot.sendKeyboard("Калькулятор", peerId, CalculatorKeyboard.KEYBOARD);
    }
    @Contract(pure = true)
    static boolean isPersonalConversation(int peerId, int fromId) {
        return peerId == fromId && history.containsKey(peerId);
    }
}
