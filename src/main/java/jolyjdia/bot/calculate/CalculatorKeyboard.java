package jolyjdia.bot.calculate;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import jolyjdia.bot.calculate.calculator.Calculator;
import jolyjdia.bot.calculate.calculator.MathFunctions;

import java.util.ArrayList;
import java.util.List;

public class CalculatorKeyboard {
    static final List<List<KeyboardButton>> BOARD = new ArrayList<>();
    private static final List<List<KeyboardButton>> EMPTY = new ArrayList<>();

    static {
        renderNumber();
        List<KeyboardButton> list = new ArrayList<>();
        EMPTY.add(list);
    }
    private static void renderNumber() {
        List<KeyboardButton> list = null;
        for(int i = 0; i < 10; ++i) {
            if(i % 4 == 0) {
                list = new ArrayList<>();
                BOARD.add(list);
            }
            list.add(create(String.valueOf(i), KeyboardButtonColor.PRIMARY));
        }
        List<KeyboardButton> operatortButtons = null;
        for(int i = 0; i < Calculator.OPERATORS.length(); ++i) {
            String c = String.valueOf(Calculator.OPERATORS.charAt(i));
            if(i % 4 == 0) {
                operatortButtons = new ArrayList<>();
                BOARD.add(operatortButtons);
            }
            operatortButtons.add(create(c, KeyboardButtonColor.PRIMARY));
        }

        List<KeyboardButton> advOperatorList = null;
        for(int i = 0; i < 8; ++i) {
            if(i % 4 == 0) {
                advOperatorList = new ArrayList<>();
                BOARD.add(advOperatorList);
            }
            advOperatorList.add(create(MathFunctions.ADV_OPERATOR_LIST[i], KeyboardButtonColor.PRIMARY));
        }
        List<KeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(create("C", KeyboardButtonColor.NEGATIVE));
        keyboardButtons.add(create("<=", KeyboardButtonColor.POSITIVE));
        keyboardButtons.add(create("=", KeyboardButtonColor.POSITIVE));
        BOARD.add(keyboardButtons);


    }
    public static KeyboardButton create(String label, KeyboardButtonColor color) {
        return new KeyboardButton().setColor(color).setAction(new KeyboardButtonAction()
                .setType(KeyboardButtonActionType.TEXT)
                .setLabel(label));
    }
}
