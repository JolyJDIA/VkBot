package jolyjdia.bot.shoutbox;

import api.JavaModule;
import api.event.EventHandler;
import api.event.Listener;
import api.event.RegisterListEvent;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
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

public class ShoutboxMain extends JavaModule implements Listener {
    private static final Pattern E = Pattern.compile("ё");
    @NonNls private static final String PREFIX = "бот, ";
    private final List<Integer> speakChat = new ArrayList<>();
    @Override
    public final void onLoad() {
        RegisterListEvent.registerEvent(this);
    }
    /**
     * ГОВНО
     */
    @EventHandler
    public final void onShout(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        String text = E.matcher(msg.getText().toLowerCase(Locale.ENGLISH)).replaceAll("е");
        if (speakChat.contains(msg.getPeerId()) || text.startsWith(PREFIX)) {
            if (Shoutbox.COSINE.similarity(text, "Хватит болтать") > 0.6) {
                speakChat.remove(msg.getPeerId());
            } else if (Shoutbox.COSINE.similarity(text, "Давай поболтаем") > 0.7) {
                speakChat.add(msg.getPeerId());
            }
            ObedientBot.sendMessage(Shoutbox.generateResponse(text), msg.getPeerId());
        }
    }
}
