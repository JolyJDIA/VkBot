package jolyjdia.bot.kubanoid;

import api.utils.KeyboardUtils;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

final class KubanoidKeyboard {
    static final List<List<KeyboardButton>> GAME = new ArrayList<>();

    @Contract(pure = true)
    private KubanoidKeyboard() {}

    static {
        final List<KeyboardButton> keyboardButtons = new ArrayList<>(3);
        keyboardButtons.add(KeyboardUtils.create("КИНУТЬ", KeyboardButtonColor.POSITIVE));
        keyboardButtons.add(KeyboardUtils.create("УЧАСТВОВАТЬ", KeyboardButtonColor.PRIMARY));
        keyboardButtons.add(KeyboardUtils.create("СТОП", KeyboardButtonColor.NEGATIVE));
        GAME.add(keyboardButtons);
    }
}
