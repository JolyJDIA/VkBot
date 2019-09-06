package jolyjdia.bot.calculate;

import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class CalculatorManager {
    private static final Map<Integer, String> history = new HashMap<>();
    @Contract(pure = true)
    private CalculatorManager() {}

    static void actionsCalculator(int peerId, @NotNull @NonNls String element) {
        switch (element) {
            case "=" -> {
                Calculator calculator = new Calculator(history.get(peerId));
                String answer = calculator.solveExpression();
                if (answer.isEmpty()) {
                    return;
                }
                closeCalculatorBoard(answer, peerId);
            }
            case "C" -> history.compute(peerId, (k, v) -> "");
            case "<=" -> {
                history.computeIfPresent(peerId, (k, v) -> v.substring(0, v.length()-1));
                String expression = history.get(peerId);
                if(expression.isEmpty()) {
                    return;
                }
                ObedientBot.sendMessage(expression, peerId);
            }
            default -> {
                history.compute(peerId, (k, v) -> v + element);
                ObedientBot.sendMessage(history.get(peerId), peerId);
            }
        }
    }
    private static void addHistory(int peerId) {
        history.put(peerId, "");
    }
    private static void removeHistory(int peerId) {
        history.remove(peerId);
    }
    @Contract(pure = true)
    static boolean containsKey(int peerId) {
        return history.containsKey(peerId);
    }
    static void closeCalculatorBoard(String text, int peerId) {
        removeHistory(peerId);
        ObedientBot.sendKeyboard(text, peerId, new Keyboard()
                .setButtons(Collections.emptyList()));
    }
    static void openCalculatorBoard(int peerId) {
        addHistory(peerId);
        ObedientBot.sendKeyboard("Калькулятор", peerId, new Keyboard()
                .setButtons(CalculatorKeyboard.BOARD));
    }
    @Contract(pure = true)
    static boolean isPersonalConversation(int peerId, int fromId) {
        return peerId == fromId && containsKey(peerId);
    }
}
