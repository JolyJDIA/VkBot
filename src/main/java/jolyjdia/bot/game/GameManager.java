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

    static void addPlayer(int peerId, int userId) {
        Map<Integer, Player> maps = map.computeIfAbsent(peerId, k -> new HashMap<>());
        maps.put(userId, new Player());
    }
    static void newIntegerPlayer(int peerId, int userId) {
        int random = (int)(Math.random() * 6) + 1;
        Player player = getPlayer(peerId, userId);
        if(player == null) {
            return;
        }
        int sum = player.addScore(random);
        ObedientBot.sendMessage(
                "[id"+userId+"|ДОДИК]"+
                "\nВаше число: "+random+
                "\nСумма: "+sum, peerId);
        if(sum > 21) {
            ObedientBot.sendMessage("ТЫ ПРОИХРАЛ BRUH", peerId);
            map.get(peerId).remove(userId);
        } else if(sum == 21) {
            ObedientBot.sendMessage("Ты выиграл", peerId);
            player.setWin(true);
        }
        if(map.get(peerId).size() == 1) {
            StringBuilder builder = new StringBuilder();
            map.get(peerId).forEach((id, p) ->
                    builder.append("# [id").append(id).append("|ДОДИК]").append('(').append(p.getScore()).append(')').append('\n'));
            ObedientBot.sendKeyboard("Конец игры!\n"+builder, peerId, new Keyboard().setButtons(Collections.emptyList()));
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
    private static Collection<Integer> getChat(int peerId) {
        return map.get(peerId).keySet();
    }
    static void removePlayer(int peerId, int userId) {
        map.get(peerId).remove(userId);
    }
    public static int getCount(int peerId) {
        return getChat(peerId).size();
    }
    @Nullable
    public static Player getPlayer(int peerId, int userId) {
        Map<Integer, Player> players = map.getOrDefault(peerId, null);
        return players != null ? players.getOrDefault(userId, null) : null;
    }
    public static void stopGame(int peerId) {
        ObedientBot.sendKeyboard("Конец игры!", peerId, new Keyboard().setButtons(Collections.emptyList()));
        if(!map.containsKey(peerId)) {
            return;
        }
        map.get(peerId).clear();
    }
}
