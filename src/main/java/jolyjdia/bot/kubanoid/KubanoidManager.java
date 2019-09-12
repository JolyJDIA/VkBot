package jolyjdia.bot.kubanoid;

import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class KubanoidManager {
    public static Map<Integer, Map<Integer, KubanoidPlayer>> map = new HashMap<>();

    @Contract(pure = true)
    private KubanoidManager() {}

    static void addPlayer(int peerId, int userId) {
        Map<Integer, KubanoidPlayer> maps = map.computeIfAbsent(peerId, k -> new HashMap<>());
        maps.put(userId, new KubanoidPlayer());
    }
    static void newIntegerPlayer(int peerId, int userId) {
        int random = (int)(Math.random() * 6) + 1;
        KubanoidPlayer player = getPlayer(peerId, userId);
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
    static Collection<KubanoidPlayer> getScore(int peerId) {
        return map.get(peerId).values();
    }
    static boolean containsPlayer(int peerId, int userId) {
        return map.containsKey(peerId) && map.get(peerId).containsKey(userId);
    }
    @NotNull
    private static Set<Integer> getChat(int peerId) {
        return map.get(peerId).keySet();
    }
    static void removePlayer(int peerId, int userId) {
        map.get(peerId).remove(userId);
    }
    public static int getCount(int peerId) {
        return getChat(peerId).size();
    }
    @Nullable
    public static KubanoidPlayer getPlayer(int peerId, int userId) {
        Map<Integer, KubanoidPlayer> players = map.getOrDefault(peerId, null);
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
