package jolyjdia.bot.calculate;

import api.utils.KeyboardUtils;
import com.google.common.collect.ImmutableList;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import jolyjdia.bot.calculate.calculator.MathFunctions;
import jolyjdia.bot.calculate.calculator.Parser;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

final class CalculatorKeyboard {
    static final List<List<KeyboardButton>> BOARD = new ArrayList<>(7);
    @Contract(pure = true)
    private CalculatorKeyboard() {}

    static {
        renderNumber();
        renderOperation();
        renderAction();
    }
    private static void renderNumber() {
        List<KeyboardButton> list = null;
        for(int i = 0; i < 10; ++i) {
            if(i % 4 == 0) {
                list = new ArrayList<>(4);
                BOARD.add(list);
            }
            list.add(KeyboardUtils.create(String.valueOf(i), KeyboardButtonColor.PRIMARY));
        }
    }
    private static void renderAction() {
        final List<KeyboardButton> keyboardButtons = ImmutableList.of(
                KeyboardUtils.create("C", KeyboardButtonColor.NEGATIVE),
                KeyboardUtils.create("<=", KeyboardButtonColor.NEGATIVE),
                KeyboardUtils.create("=", KeyboardButtonColor.NEGATIVE)
        );
        BOARD.add(keyboardButtons);
    }
    private static void renderOperation() {
        List<KeyboardButton> operatortButtons = null;
        for(int i = 0; i < Parser.OPERATORS.length(); ++i) {
            String c = String.valueOf(Parser.OPERATORS.charAt(i));
            if(i % 4 == 0) {
                operatortButtons = new ArrayList<>(4);
                BOARD.add(operatortButtons);
            }
            operatortButtons.add(KeyboardUtils.create(c, KeyboardButtonColor.PRIMARY));
        }
        final List<KeyboardButton> advOperatorList = new ArrayList<>(4);
        for(int i = 0; i < 4; ++i) {
            advOperatorList.add(KeyboardUtils.create(MathFunctions.ADV_OPERATOR_LIST[i], KeyboardButtonColor.PRIMARY));
        }
        BOARD.add(advOperatorList);
    }
}
