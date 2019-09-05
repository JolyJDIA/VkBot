package jolyjdia.bot.calculate;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;

import java.util.ArrayList;
import java.util.List;

public class CalculatorKeyboard {
    public static final List<List<KeyboardButton>> BOARD = new ArrayList<>();
    public static final List<List<KeyboardButton>> EMPTY = new ArrayList<>();

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
        list.add(create(")", KeyboardButtonColor.PRIMARY));
        list.add(create("(", KeyboardButtonColor.PRIMARY));

        List<KeyboardButton> operatortButtons = new ArrayList<>();
        operatortButtons.add(create("+", KeyboardButtonColor.PRIMARY));
        operatortButtons.add(create("-", KeyboardButtonColor.PRIMARY));
        operatortButtons.add(create("/", KeyboardButtonColor.PRIMARY));
        operatortButtons.add(create("*", KeyboardButtonColor.PRIMARY));
        BOARD.add(operatortButtons);

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
