package api.utils;

import com.vk.api.sdk.objects.messages.*;
import org.jetbrains.annotations.Contract;

import java.util.Collections;

public final class KeyboardUtils {
    public static final Keyboard EMPTY_KEYBOARD = new Keyboard().setButtons(Collections.emptyList());
    @Contract(pure = true)
    private KeyboardUtils() {}

    public static KeyboardButton create(String label, KeyboardButtonColor color) {
        return new KeyboardButton().setColor(color).setAction(new KeyboardButtonAction()
                .setType(KeyboardButtonActionType.TEXT)
                .setLabel(label));
    }
}
