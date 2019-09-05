package jolyjdia.bot.calculate;

import api.event.EventHandler;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.bot.calculate.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CalculatorListener implements Listener {
    private static final Pattern MATH = Pattern.compile("[a-zA-Z.\\d+\\-*/()^< ]*");
    @EventHandler
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        if(MATH.matcher(msg.getText()).matches()) {
            long start = System.currentTimeMillis();
            Calculator calc = new Calculator(msg.getText());
            String answer = calc.solveExpression();
            long end = System.currentTimeMillis() - start;
            System.out.println(end);
            if(answer.isEmpty()) {
                return;
            }
            ObedientBot.sendMessage(answer, msg.getPeerId());
        }
    }
    /**
     * private static final Map<Integer, List<String>> history = new HashMap<>();
     *         if(CalculatorCommand.calculator.contains(peerId)) {
     *             String s = msg.getText().split(" ")[1];
     *             switch (s) {
     *                 case "=" -> {
     *                     StringBuilder builder = new StringBuilder();
     *                     for (String h : history.get(peerId)) {
     *                         builder.append(h);
     *                     }
     *                     Calculator calculator = new Calculator(builder.toString());
     *                     String answer = calculator.solveExpression();
     *                     if (answer.isEmpty()) {
     *                         return;
     *                     }
     *                     ObedientBot.sendMessage(answer, peerId);
     *                     history.clear();
     *                 }
     *                 case "C" -> history.get(peerId).clear();
     *                 case "<=" -> {
     *                     if(!history.containsKey(peerId)) {
     *                         return;
     *                     }
     *                     if(history.get(peerId).isEmpty()) {
     *                         return;
     *                     }
     *                     history.get(peerId).remove(history.size() - 1);
     *                 }
     *             }
     *             if(history.containsKey(peerId)) {
     *                 List<String> list = history.get(peerId);
     *                 list.add(s);
     *                 history.put(peerId, list);
     *             } else {
     *                 history.put(peerId, new ArrayList<>());
     *             }
     *         }
     */
}
