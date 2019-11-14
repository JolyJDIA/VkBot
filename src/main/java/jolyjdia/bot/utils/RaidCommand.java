package jolyjdia.bot.utils;

import api.command.Command;
import api.scheduler.RoflanRunnable;
import api.storage.User;
import api.utils.StringBind;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RaidCommand extends Command {
    private final Map<Integer, RoflanRunnable> raids = Maps.newHashMap();

    @NonNls private static final String CHELIBOSI =
            "[id357961738|УНТЕРМЕНШ]\n[id192559701|БОЙ НЭКСТ ДОР]\n[id503903106|МИКРОЧЕЛ]\n[id481298154|КУЛЕБЯКА]\n"
            .repeat(35);

    RaidCommand() {
        super("raid");
        setPermission("roflanboat.raid", "Ты кто такой, чтобы это сделать? Команда не найдена, крч\nВведи пароль: /access <Пароль>");
        setAlias("access");
    }

    private static final String PASSWORD = "boat";
    @Contract(pure = true)
    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        if(args[0].equalsIgnoreCase("access")) {
            if(!args[1].equalsIgnoreCase(PASSWORD)) {
                sender.sendMessageFromChat("Даун, ливни из жизни");
                return;
            }
            sender.sendMessageFromChat("Успешно)");
            STAFF_ADMIN.put("NotFound", sender.getUserId());
        } else {
            if(noPermission(sender)) {
                return;
            }
            if (args.length == 1) {
                if (raids.containsKey(sender.getPeerId())) {
                    sender.sendMessageFromChat("Рейд уже запущен");
                    return;
                }
                startRaid(sender, CHELIBOSI, 3);
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("stop")) {
                    if (!raids.containsKey(sender.getPeerId())) {
                        sender.sendMessageFromChat("Рейд еще не запущен");
                        return;
                    }
                    RoflanRunnable runnable = raids.remove(sender.getPeerId());
                    runnable.cancel();
                    sender.sendMessageFromChat("ZA WARDO");
                }
            } else {
                int period;
                try {
                    period = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    period = 3;
                }
                if (period < 2) {
                    sender.sendMessageFromChat("Ошибка! Слишком маленькая задержка, начинай с 2-х");
                    return;
                }
                @NonNls String text = StringBind.toString(2, args) + '\n';
                text = text.repeat(lenghtNotify(text.length()));
                startRaid(sender, text, period);
            }
        }
    }
    @Contract(pure = true)
    private static int lenghtNotify(int lenghtSource) {
        return 2500/lenghtSource;
    }
    private final void startRaid(@NotNull User user, String text, int period) {
        if(raids.containsKey(user.getPeerId())) {
            user.sendMessageFromChat("В этой беседе уже идет рейд!");
            return;
        }
        RaidRunnable raidRunnable = new RaidRunnable(user, text);
        raidRunnable.runTaskTimer(0, period);
        raids.put(user.getPeerId(), raidRunnable);
    }

    private static final class RaidRunnable extends RoflanRunnable {
        private final User user;
        private final String message;

        private RaidRunnable(User user, String message) {
            this.user = user;
            this.message = message;
        }

        @Override
        public void run() {
            user.sendMessageFromChat(message);
        }
    }
}
