package jolyjdia.bot.game;

import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GameManager {
    public static final Map<Integer, Map<Integer, Player>> map = new HashMap<>();

    @Contract(pure = true)
    private GameManager() {}

    public static int getRandom() {
        return (int)(Math.random() * 6) + 1;
    }
    static void addPlayer(int peerId, int userId) {
        map.putIfAbsent(peerId, new HashMap<>());
        map.get(peerId).put(userId, new Player());
    }
    static void newIntegerPlayer(int peerId, int userId) {
        int random = getRandom();
        int sum = map.get(peerId).get(userId).addScore(random);
        ObedientBot.sendMessage(
                "[id"+userId+"|ДОДИК]"+
                "\nВаше число: "+random+
                "\nСумма: "+sum, peerId);
        if(sum > 21) {
            ObedientBot.sendMessage("ТЫ ПРОИХРАЛ BRUH", peerId);
            map.get(peerId).remove(userId);
        } else if(sum == 21) {
            ObedientBot.sendMessage("Ты выиграл", peerId);
            map.get(peerId).get(userId).setWin(true);
        }
        if(map.get(peerId).size() == 1) {
            StringBuilder builder = new StringBuilder();
            map.get(peerId).forEach((id, p) ->
                    builder.append("# [id").append(id).append("|ДОДИК]").append('(').append(p.getScore()).append(')').append('\n'));
            ObedientBot.sendKeyboard("Конец игры!\n"+builder, peerId, new Keyboard().setButtons(Collections.emptyList()));
        }
    }
    @Nullable
    static Player getStatePlayer(int peerId, int userId) {
        if(!map.containsKey(peerId)) {
            return null;
        }
        if(!map.get(peerId).containsKey(userId)) {
            return null;
        }
        return map.get(peerId).get(userId);
    }
    static void stop(String win, int peerId, int userId) {
        ObedientBot.sendKeyboard(win, peerId, new Keyboard().setButtons(Collections.emptyList()));
        if(map.containsKey(peerId)) {
            map.get(peerId).remove(userId);
        }
    }
    @NotNull
    static Collection<Player> getScore(int peerId) {
        return map.get(peerId).values();
    }
    static boolean containsPlayer(int peerId, int userId) {
        return map.containsKey(peerId) && map.get(peerId).containsKey(userId);
    }
    @NotNull
    static Collection<Integer> getChat(int peerId) {
        return map.get(peerId).keySet();
    }
    static void removePlayer(int peerId, int userId) {
        map.get(peerId).remove(userId);
    }
}
