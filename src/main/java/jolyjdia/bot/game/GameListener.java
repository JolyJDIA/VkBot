package jolyjdia.bot.game;

import api.event.EventHandler;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GameListener implements Listener {
    @EventHandler
    public static void onSend(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        int peerId = msg.getPeerId();
        if(peerId != 2000000001) {
            return;
        }
        int userId = msg.getFromId();
        if(msg.getText().contains("УЧАСТВОВАТЬ")) {
            GameManager.addPlayer(peerId, userId);
            ObedientBot.sendMessage("Твой номер: "+ count(peerId), peerId);
        }
        if(!GameManager.containsPlayer(peerId, userId)) {
            return;
        }
        if(msg.getText().contains("КИНУТЬ")) {//колумбайн//колумbruh//колумbruhйн
            int random = GameManager.getRandom();
            @NonNls String s = "[id"+userId+"|ДОДИК]";
            ObedientBot.sendMessage(s+
                    "\nВаше число: "+random+
                    "\nСумма: "+GameManager.getStatePlayer(peerId, userId), peerId);
            GameManager.addScorePlayer(peerId, userId, random);

        } else if(msg.getText().contains("СТОП")) {
            @NonNls String s = "[id"+userId+"|ДОДИК]";
            ObedientBot.sendMessage(s + "\nОчков: " + GameManager.getStatePlayer(peerId, userId), peerId);
            GameManager.removePlayer(peerId, userId);
        }
    }
    @Contract(pure = true)
    private static int count(int peerId) {
        for(int i = 0; i < GameManager.getChat(peerId).size(); ++i) {
            if(i == GameManager.getChat(peerId).size()-1) {
                return i;
            }
        }
        return 0;
    }
}
