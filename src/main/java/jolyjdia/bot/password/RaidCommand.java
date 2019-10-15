package jolyjdia.bot.password;

import api.command.Command;
import api.scheduler.RoflanRunnable;
import api.storage.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RaidCommand extends Command {
    private final Map<Integer, RoflanRunnable> raids = new HashMap<>();

    @NonNls private static final String CHELIBOSI =
            "[id357961738|УНТЕРМЕНШ]\n[id192559701|БОЙ НЭКСТ ДОР]\n[id503903106|МИКРОЧЕЛ]\n[id481298154|КУЛЕБЯКА]\n".repeat(35);

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
            RaidRunnable raidRunnable = new RaidRunnable(user, CHELIBOSI);
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
