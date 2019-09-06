package jolyjdia.bot.calculate.calculator;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.Map;

public class ConvertConstants {
    private static final Map<String, Double> DOUBLE_MAP = Maps.newHashMap();
    @NonNls private final List<? super String> userInpList;
    static {
        DOUBLE_MAP.put("pi", Math.PI);
        DOUBLE_MAP.put("e", Math.E);
        DOUBLE_MAP.put("tau", 6.283185307179586);
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