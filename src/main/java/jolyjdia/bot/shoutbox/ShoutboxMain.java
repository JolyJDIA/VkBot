package jolyjdia.bot.shoutbox;

import api.Bot;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * ГОВНО
 */

public class ShoutboxMain implements Module, Listener {
    private static final Pattern E = Pattern.compile("ё");
    @NonNls private static final String PREFIX = "бот, ";
    private final List<Integer> speakChat = new ArrayList<>();
    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
    }
    @EventLabel
    public final void onShout(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        String text = E.matcher(msg.getText().toLowerCase(Locale.ENGLISH)).replaceAll("е");
        if (speakChat.contains(msg.getPeerId()) || text.startsWith(PREFIX)) {
            if (Shoutbox.COSINE.similarity(text, "хватит болтать") > 0.6) {
                speakChat.remove(msg.getPeerId());
            } else if (Shoutbox.COSINE.similarity(text, "давай поболтаем") > 0.7) {
                speakChat.add(msg.getPeerId());
            }
            Bot.sendMessage(Shoutbox.generateResponse(text), msg.getPeerId());
        }
    }
}
