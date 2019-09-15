package jolyjdia.bot.raid;

import api.utils.KeyboardUtils;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public final class RaidKeyboard {
    public static final List<List<KeyboardButton>> BOARD = new ArrayList<>();

    static {
        List<KeyboardButton> list = null;
        boolean b = false;
        for (int i = 0; i < 24; ++i) {
            if (i % 4 == 0) {
                list = new ArrayList<>(4);
                BOARD.add(list);
                b = !b;
            }
            KeyboardButtonColor color = i % 2 == (b ? 0 : 1) ? KeyboardButtonColor.POSITIVE : KeyboardButtonColor.NEGATIVE;
            list.add(KeyboardUtils.create("ЛОДКА", color));
        }
    }

    @Contract(pure = true)
    private RaidKeyboard() {}
}
