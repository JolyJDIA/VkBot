package jolyjdia.bot.password;

import api.command.Command;
import api.scheduler.RoflanRunnable;
import api.storage.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RaidCommand extends Command {
    private final Map<Integer, RoflanRunnable> raids = new HashMap<>();
    private static final String[] NAMES = {
            "УНТЕРМЕНШ",
            "БОЙ НЭКСТ ДОР",
            "МИКРОЧЕЛ",
            "КУЛЕБЯКА"
    };
    @NonNls private static final String DAYNI = "[id357961738|" + NAMES[MathUtils.RANDOM.nextInt(NAMES.length)] +
            "]\n[id192559701|" + NAMES[MathUtils.RANDOM.nextInt(NAMES.length)] +
            "]\n[id503903106|" + NAMES[MathUtils.RANDOM.nextInt(NAMES.length)] +
            "]\n[id481298154|" + NAMES[MathUtils.RANDOM.nextInt(NAMES.length)] + "]\n".repeat(35);


    RaidCommand() {
        super("raid");
    }

    @Contract(pure = true)
    @Override
    public final void execute(User user, @NotNull String[] args) {
        if(args.length == 1) {
            if(raids.containsKey(user.getPeerId())) {
                user.sendMessageFromChat("Рейд уже запущен");
                return;
            }
            RaidRunnable raidRunnable = new RaidRunnable(user, DAYNI);
            raidRunnable.runTaskTimer(0, 2);

            raids.put(user.getPeerId(), raidRunnable);
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("stop")) {
                if(!raids.containsKey(user.getPeerId())) {
                    user.sendMessageFromChat("Рейд еще не запущен");
                    return;
                }
                RoflanRunnable runnable = raids.remove(user.getPeerId());
                runnable.cancel();
            }
        }
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

    /*Loader.getVkApiClient()
                        .messages().getConversationMembers(Bot.getGroupActor(), user.getPeerId())
                        .execute()
                        .getItems()
                        .stream().filter(e -> {
                            int id = e.getMemberId();
                            return id > 0 && (id != 323998691
                                    && id != 190345817
                                    && id != 310289867
                                    && id != 199686399
                                    && id != 442053514
                                    && id != 526616439
                                    && id != 276992948//Настя
                                    && id != 541147678//FreshTea

                                    //КЛАСС
                                    && id != 160280940
                                    && id != 146944869

                            );
                        })*/
