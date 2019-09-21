package jolyjdia.bot.password;

import api.Bot;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.SendCommandEvent;
import api.module.Module;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class GeneratorPassword implements Module, Listener {
    private final Map<Integer, Long> cooldown = new WeakHashMap<>();

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new PasswordCommand());
        Bot.getBotManager().registerCommand(new RaidCommand());
        Bot.getBotManager().registerEvent(this);
    }
    @EventLabel
    public final void onCommand(@NotNull SendCommandEvent e) {
        int userId = e.getUser().getUserId();
        if(cooldown.containsKey(userId) && cooldown.get(userId) > System.currentTimeMillis()) {
            e.getUser().sendMessageFromHisChat("Подождите 1 секунду, перед тем, как использовать команду снова");
            e.setCancelled(true);
            return;
        } else {
            cooldown.remove(userId);
        }
        cooldown.put(userId, System.currentTimeMillis() + 1000);
    }
}
