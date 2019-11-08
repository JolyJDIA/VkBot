package jolyjdia.bot.smile;

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
import jolyjdia.bot.Bot;
import jolyjdia.bot.shoutbox.similarity.Cosine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SmileLoad implements Module, Listener {
    private static final Map<String, String> SMILIES = ImmutableMap.<String, String>builder()
            .put("54inside", "-178836630_457239417")
            .put("smallinside", "-178836630_457239421")
            .put("chtoblyat", "-178836630_457239418")
            .put("din", "-178836630_457239420")
            .put("ban", "-178836630_457239393")
            .put("god", "-178836630_457239399")
            .put("dawg", "-178836630_457239392")
            .put("sit", "-178836630_457239410")
            .put("bottle", "-178836630_457239398")
            .put("uk", "-178836630_457239415")
            .put("plak", "-178836630_457239411")
            .put("bb", "-178836630_457239390")
            .put("roflanebalo", "-178836630_457239419")
            .put("ledokolchik", "-178836630_457239396")
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
