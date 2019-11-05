package jolyjdia.bot.smile;

import api.Bot;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import api.utils.KeyboardUtils;
import api.utils.StringBind;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import jolyjdia.bot.shoutbox.similarity.Cosine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SmileLoad implements Module, Listener {
    private static final Map<String, String> SMILIES = ImmutableMap.<String, String>builder()
            .put("54inside", "310289867_457244162")
            .put("smallinside", "310289867_457244164")
            .put("chtoblyat", "310289867_457244165")
            .put("din", "310289867_457244163")
            .put("ban", "310289867_457244151")
            .put("god", "310289867_457244147")
            .put("dawg", "310289867_457244146")
            .put("sit", "310289867_457244143")
            .put("bottle", "310289867_457244149")
            .put("uk", "310289867_457244145")
            .put("plak", "310289867_457244150")
            .put("bb", "310289867_457244175")
            .put("roflanebalo", "310289867_457244176")
            .put("boat", "310289867_457244257")
            .put("ledokolchik", "526616439_457240875")
            .build();
    private static final ImmutableList.Builder<List<KeyboardButton>> BOARD = ImmutableList.builder();

    static {
        List<KeyboardButton> list = null;
        int i = 0;
        for(String label : SMILIES.keySet()) {
            if(i % 4 == 0) {
                list = new ArrayList<>();
                BOARD.add(list);
            }
            list.add(KeyboardUtils.create(':'+label+':', KeyboardButtonColor.PRIMARY));
            ++i;
        }
    }
    static final Keyboard KEYBOARD = new Keyboard().setButtons(BOARD.build());
    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
        Bot.getBotManager().registerCommand(new SmileCommand());
    }
    private static final Cosine COSINE = new Cosine(3);
    @EventLabel
    public static void onMsg(@NotNull NewMessageEvent e) {
        String text = e.getMessage().getText().toLowerCase(Locale.ENGLISH);
        if(text.isEmpty()) {
            return;
        }
        if(COSINE.similarity(text, "леша") >= 0.6 || COSINE.similarity(text, "лёша") >= 0.6) {
            e.getUser().sendMessageFromChat(null, "audio310289867_456241810");
        }
        List<String> smiles = StringBind.substringsBetween(text, ":", ":");
        if(smiles == null || smiles.isEmpty()) {
            return;
        }
        smiles.forEach(smile -> {
            if(SMILIES.containsKey(smile)) {
                e.getUser().sendMessageFromChat(null, "photo" + SMILIES.get(smile));
            }
        });
    }
}
