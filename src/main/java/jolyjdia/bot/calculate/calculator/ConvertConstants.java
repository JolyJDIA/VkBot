package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertConstants {
    private static final Map<String, Double> DOUBLE_MAP = new HashMap<>(2);
    @NonNls private final List<? super String> userInpList;
    static {
        DOUBLE_MAP.put("pi", Math.PI);
        DOUBLE_MAP.put("e", Math.E);
    }

    @Contract(pure = true)
    public ConvertConstants(List<? super String> userInpList) {
        this.userInpList = userInpList;
    }

    public final void convert() {
        for (Map.Entry<String, Double> entry : DOUBLE_MAP.entrySet()) {
            String constant = entry.getKey();
            for (int i = 0; i < userInpList.size(); i++) {
                if (userInpList.get(i).equals(constant)) {
                    userInpList.set(i, String.valueOf(entry.getValue()));
                } else if (userInpList.get(i).equals('-' + constant)) {
                    userInpList.set(i, "-" + entry.getValue());
                }
            }
        }
    }
}