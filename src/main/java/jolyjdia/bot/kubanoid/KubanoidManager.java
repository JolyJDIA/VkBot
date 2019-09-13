package jolyjdia.bot.kubanoid;

import api.utils.KeyboardUtils;
import api.utils.ObedientBot;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
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
        String nick = getPlayerName(userId);
        ObedientBot.sendMessage("Ваше число: "+random+ "\nСумма: "+sum, userId);
        if(sum > 21) {
            ObedientBot.sendMessage(nick +" ПРОИГРАЛ", peerId);
            map.get(peerId).remove(userId);
        } else if(sum == 21) {
            ObedientBot.sendMessage(nick+" ВЫИГРАЛ", peerId);
            player.setWin(true);
        }
        if(map.get(peerId).size() == 1) {
            StringBuilder builder = new StringBuilder();
            map.get(peerId).forEach((id, p) ->
                    builder.append(nick).append(" Очнов: ").append(p.getScore()).append('\n'));
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
    @NotNull
    @NonNls
    public static String getPlayerName(int userId) {
        try {
            UserXtrCounters user = Bot.getVkApiClient().users().get(new UserActor(userId, Bot.ACCESS_TOKEN))
                    .userIds(String.valueOf(userId)).execute().get(0);
            return user.getFirstName() + ' ' +user.getLastName();
        } catch (ClientException | ApiException ex) {
            ex.printStackTrace();
        }
        return "";
    }
    static void stopGame(int peerId) {
        ObedientBot.sendKeyboard("Конец игры!", peerId, KeyboardUtils.EMPTY_KEYBOARD);
        if(!map.containsKey(peerId)) {
            return;
        }
        map.get(peerId).clear();
    }
}
