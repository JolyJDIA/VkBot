package jolyjdia.bot.calculate;

import api.utils.KeyboardUtils;
import com.google.common.collect.ImmutableList;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import jolyjdia.bot.calculate.calculator.Parser;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

final class CalculatorKeyboard {
    private static final ImmutableList.Builder<List<KeyboardButton>> BOARD = ImmutableList.builder();
    static Keyboard KEYBOARD;
    @Contract(pure = true)
    private CalculatorKeyboard() {}

    static {
        List<KeyboardButton> list = null;
        int sumb = -1;
        int num = 10;
        for(int i = 0; i < 12; ++i) {
            if(i % 4 == 0) {
                list = new ArrayList<>();
                list.add(KeyboardUtils.create(String.valueOf(Parser.OPERATORS.charAt(++sumb)), KeyboardButtonColor.NEGATIVE));
                BOARD.add(list);
            } else {
                list.add(KeyboardUtils.create(String.valueOf(--num), KeyboardButtonColor.POSITIVE));
            }
        }
        BOARD.add(ImmutableList.of(
                KeyboardUtils.create("/", KeyboardButtonColor.NEGATIVE),
                KeyboardUtils.create("=", KeyboardButtonColor.NEGATIVE),
                KeyboardUtils.create("0", KeyboardButtonColor.POSITIVE),
                KeyboardUtils.create(".", KeyboardButtonColor.NEGATIVE))
        );
        KEYBOARD = new Keyboard().setButtons(BOARD.build());
    }
}
