package jolyjdia.bot.password;

import api.command.Command;
import api.scheduler.RoflanRunnable;
import api.storage.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RaidCommand extends Command {
    private static final String[] NAMES = {
            "УНТЕРМЕНШ",
            "БОЙ НЭКСТ ДОР",
            "МИКРОЧЕЛ",
            "КУЛЕБЯКА"
    };

    private RaidRunnable runnable;
    RaidCommand() {
        super("raid");
    }

    @Contract(pure = true)
    @Override
    public final void execute(User user, @NotNull String[] args) {
        if(args.length == 1) {
                StringBuilder builder = new StringBuilder();
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
                        builder.append("[id357961738|").append(NAMES[MathUtils.RANDOM.nextInt(NAMES.length)])
                                .append("]\n").append("[id192559701|").append(NAMES[MathUtils.RANDOM.nextInt(NAMES.length)])
                                .append("]\n[id503903106|").append(NAMES[MathUtils.RANDOM.nextInt(NAMES.length)])
                                .append("]\n[id481298154|").append(NAMES[MathUtils.RANDOM.nextInt(NAMES.length)]).append("]\n");
                if(builder.length() <= 0) {
                    return;
                }
                runnable = new RaidRunnable(user, builder.toString().repeat(35));
                runnable.runTaskTimer(0, 2);

        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("stop")) {
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
            user.sendMessageFromHisChat(message);
        }
    }
}
