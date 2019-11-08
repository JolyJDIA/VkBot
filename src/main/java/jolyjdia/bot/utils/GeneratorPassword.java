package jolyjdia.bot.utils;

import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.SendCommandEvent;
import api.module.Module;
import com.google.common.collect.Maps;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GeneratorPassword implements Module, Listener {
    private final Map<Integer, Long> cooldown = Maps.newHashMap();

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new PasswordCommand());
        Bot.getBotManager().registerCommand(new RaidCommand());
        Bot.getBotManager().registerCommand(new RollCommand());
        Bot.getBotManager().registerCommand(new FlipCommand());
        Bot.getBotManager().registerEvent(this);

        Bot.getScheduler().scheduleSyncRepeatingTask(() ->
                        cooldown.entrySet().removeIf(e -> (e.getValue() - System.currentTimeMillis()) < 1L),
                0, 50);
    }

    @EventLabel(ignoreCancelled = true)
    public final void onCommand(@NotNull SendCommandEvent e) {
        int userId = e.getUser().getUserId();
        if (cooldown.containsKey(userId)) {
            e.getUser().sendMessageFromChat("Подождите 1 секунду, перед тем, как использовать команду снова");
            e.setCancelled(true);
        } else {
            cooldown.put(userId, System.currentTimeMillis());
        }
    }
}
