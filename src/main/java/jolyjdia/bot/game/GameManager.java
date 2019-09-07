package jolyjdia.bot.game;

import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class GameManager {
    public static final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

    @Contract(pure = true)
    private GameManager() {}

    public static int getRandom() {
        return (int)(Math.random() * 6) + 1;
    }
    static void addPlayer(int peerId, int userId) {
        map.putIfAbsent(peerId, new TreeMap<>());
        map.get(peerId).put(userId, 0);
    }
    static void addScorePlayer(int peerId, int userId, int sum) {
        int s = map.get(peerId).get(userId)+sum;
        map.get(peerId).put(userId, s);
        if(s > 21) {
            ObedientBot.sendMessage("ТЫ ПРОИХРАЛ BRUH", peerId);
            map.get(peerId).remove(userId);
        } else if(s == 21) {
            map.get(peerId).values().stream().sorted(Integer::compareTo).limit(3).forEach(
                    top -> ObedientBot.sendMessage("ОЧКИ " + top, peerId));
            stop("ТЫ ВЫИХРАЛ BRUH", peerId);
        }
    }
    static int getStatePlayer(int peerId, int userId) {
        if(!map.containsKey(peerId)) {
            return 0;
        }
        if(!map.get(peerId).containsKey(userId)) {
            return 0;
        }
        return map.get(peerId).get(userId);
    }
    static void stop(String win, int peerId) {
        ObedientBot.sendKeyboard(win, peerId, new Keyboard().setButtons(Collections.emptyList()));
        if(map.containsKey(peerId)) {
            map.get(peerId).clear();
        }
    }
    @NotNull
    static Collection<Integer> getScore(int peerId) {
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
