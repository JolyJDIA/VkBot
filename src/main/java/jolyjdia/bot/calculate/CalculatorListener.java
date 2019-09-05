package jolyjdia.bot.calculate;

import api.event.EventHandler;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CalculatorListener implements Listener {
    private static final Map<Integer, List<String>> history = new HashMap<>();
    private static final Pattern MATH = Pattern.compile("[a-zA-Z.\\d+\\-*/()^< ]*");
    @EventHandler
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        int peerId = msg.getPeerId();
        if (peerId == msg.getFromId()) {
            actionsCalculator(peerId, msg.getText());
            return;
        }
        if (MATH.matcher(msg.getText()).matches()) {
            long start = System.currentTimeMillis();
            Calculator calc = new Calculator(msg.getText());
            String answer = calc.solveExpression();
            long end = System.currentTimeMillis() - start;
            System.out.println(end);
            if (answer.isEmpty()) {
                return;
            }
            ObedientBot.sendMessage(answer, peerId);
        }
    }
    private static void actionsCalculator(int peerId, String element) {
        if (!history.containsKey(peerId)) {
            return;
        }
        switch (element) {
            case "=" -> {
                StringBuilder builder = new StringBuilder();
                for (String h : history.get(peerId)) {
                    builder.append(h);
                }
                Calculator calculator = new Calculator(builder.toString());
                String answer = calculator.solveExpression();
                if (answer.isEmpty()) {
                    return;
                }
                ObedientBot.sendMessage(answer, peerId);
                history.clear();
            }
            case "C" -> history.get(peerId).clear();
            case "<=" -> {
                if (!history.containsKey(peerId)) {
                    return;
                }
                if (history.get(peerId).isEmpty()) {
                    return;
                }
                history.get(peerId).remove(history.size() - 1);
            }
            default -> {
                List<String> list = history.get(peerId);
                list.add(element);
                history.put(peerId, list);
            }
        }
    }
    static void addHistory(int peerId) {
        history.put(peerId, new ArrayList<>());
    }
    static void removeHistory(int peerId) {
        history.remove(peerId);
    }
    @Contract(pure = true)
    static boolean containsKey(int peerId) {
        return history.containsKey(peerId);
    }
}
