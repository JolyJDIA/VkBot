package jolyjdia.bot.kubanoid;

import api.Bot;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NotNull;

public class KubanoidLoad implements Module, Listener {

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
        Bot.getBotManager().registerCommand(new KubanoidCommand());
        Bot.getBotManager().registerCommand(new RollCommand());
        Bot.getBotManager().registerCommand(new FlipCommand());
    }
    @EventLabel
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        if(msg.getText().isEmpty()) {
            return;
        }
        int peerId = msg.getPeerId();
        int userId = msg.getFromId();
        if(KubanoidManager.containsPlayer(peerId, userId)) {
            if(msg.getText().equalsIgnoreCase("КИНУТЬ")) {//колумбайн//колумbruh//колумbruhйн
                KubanoidManager.newRandomAndCreatePlayer(peerId, userId);
            }
        } else {
            if (msg.getText().equalsIgnoreCase("УЧАСТВОВАТЬ")) {
                KubanoidManager.addPlayer(peerId, userId);
                e.getUser().sendMessageFromHisChat("Твой номер: " + KubanoidManager.getCount(peerId));
            }
        }
    }
}
