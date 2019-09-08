package jolyjdia.bot.game;

import api.JavaModule;
import api.command.RegisterCommandList;
import api.event.EventHandler;
import api.event.Listener;
import api.event.RegisterListEvent;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class GameLoad extends JavaModule implements Listener {

    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new GameCommand());
        RegisterListEvent.registerEvent(this);
        RegisterCommandList.registerCommand(new RollCommand());
    }
    @EventHandler
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        int peerId = msg.getPeerId();
        int userId = msg.getFromId();
        if(GameManager.containsPlayer(peerId, userId)) {
            if(msg.getText().contains("КИНУТЬ")) {//колумбайн//колумbruh//колумbruhйн
                GameManager.newIntegerPlayer(peerId, userId);

            } else if(msg.getText().contains("СТОП")) {
                ObedientBot.sendKeyboard("Конец игры!", peerId, new Keyboard().setButtons(Collections.emptyList()));
            }
        } else {
            if (msg.getText().contains("УЧАСТВОВАТЬ")) {
                GameManager.addPlayer(peerId, userId);
                ObedientBot.sendMessage("Твой номер: " + GameManager.getCount(peerId), peerId);
            }
        }
    }
}
