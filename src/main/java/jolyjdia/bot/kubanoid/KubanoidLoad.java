package jolyjdia.bot.kubanoid;

import api.JavaModule;
import api.command.RegisterCommandList;
import api.event.EventLabel;
import api.event.Listener;
import api.event.RegisterListEvent;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NotNull;

public class KubanoidLoad extends JavaModule implements Listener {

    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new KubanoidCommand());
        RegisterListEvent.registerEvent(this);
        RegisterCommandList.registerCommand(new RollCommand());
        RegisterCommandList.registerCommand(new FlipCommand());
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
            if(msg.getText().contains("КИНУТЬ")) {//колумбайн//колумbruh//колумbruhйн
                KubanoidManager.newIntegerPlayer(peerId, userId);

            } else if(msg.getText().contains("СТОП")) {
                KubanoidManager.stopGame(peerId);
            }
        } else {
            if (msg.getText().contains("УЧАСТВОВАТЬ")) {
                KubanoidManager.addPlayer(peerId, userId);
                ObedientBot.sendMessage("Твой номер: " + KubanoidManager.getCount(peerId), peerId);
            }
        }
    }
}
