package jolyjdia.bot.calculate;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import jolyjdia.bot.calculate.calculator.Calculator;
import jolyjdia.bot.calculate.calculator.MathFunctions;

import java.util.ArrayList;
import java.util.List;

class CalculatorKeyboard {
    static final List<List<KeyboardButton>> BOARD = new ArrayList<>(7);

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
            list.add(create(String.valueOf(i), KeyboardButtonColor.PRIMARY));
        }
    }
    private static void renderAction() {
        List<KeyboardButton> keyboardButtons = new ArrayList<>(3);
        keyboardButtons.add(create("C", KeyboardButtonColor.NEGATIVE));
        keyboardButtons.add(create("<=", KeyboardButtonColor.NEGATIVE));
        keyboardButtons.add(create("=", KeyboardButtonColor.NEGATIVE));
        BOARD.add(keyboardButtons);
    }
    private static void renderOperation() {
        List<KeyboardButton> operatortButtons = null;
        for(int i = 0; i < Calculator.OPERATORS.length(); ++i) {
            String c = String.valueOf(Calculator.OPERATORS.charAt(i));
            if(i % 4 == 0) {
                operatortButtons = new ArrayList<>(4);
                BOARD.add(operatortButtons);
            }
            operatortButtons.add(create(c, KeyboardButtonColor.PRIMARY));
        }
        List<KeyboardButton> advOperatorList = new ArrayList<>(4);
        for(int i = 0; i < 4; ++i) {
            advOperatorList.add(create(MathFunctions.ADV_OPERATOR_LIST[i], KeyboardButtonColor.PRIMARY));
        }
        BOARD.add(advOperatorList);
    }
    private static KeyboardButton create(String label, KeyboardButtonColor color) {
        return new KeyboardButton().setColor(color).setAction(new KeyboardButtonAction()
                .setType(KeyboardButtonActionType.TEXT)
                .setLabel(label));
    }
}
