package jolyjdia.bot.password;

import api.command.Command;
import api.scheduler.RoflanRunnable;
import api.storage.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RaidCommand extends Command {
    private static final String RAID =
            "[id190345817|ЮДЖИН]\n[id323998691|БОГАРДО]\n[id310289867|ЗАВР]\n[id199686399|ЛЕХА]\n".repeat(50);

    private RaidRunnable runnable;
    public RaidCommand() {
        super("raid");
    }

    @Contract(pure = true)
    @Override
    public final void execute(User user, @NotNull String[] args) {
        if(args.length == 1) {
            runnable = new RaidRunnable(user);
            runnable.runTaskTimer(0, 10);
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("stop")) {
                runnable.cancel();
            }
        }
    }

    private static final class RaidRunnable extends RoflanRunnable {
        private final User user;

        private RaidRunnable(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            user.sendMessageFromHisChat(RAID);
        }
    }
}
