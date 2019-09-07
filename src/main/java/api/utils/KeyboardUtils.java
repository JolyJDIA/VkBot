package api.utils;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;

public final class KeyboardUtils {
    private KeyboardUtils() {}

    public static KeyboardButton create(String label, KeyboardButtonColor color) {
        return new KeyboardButton().setColor(color).setAction(new KeyboardButtonAction()
                .setType(KeyboardButtonActionType.TEXT)
                .setLabel(label));
    }
}
