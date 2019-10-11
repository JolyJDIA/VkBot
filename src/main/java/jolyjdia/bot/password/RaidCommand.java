package jolyjdia.bot.password;

import api.Bot;
import api.command.Command;
import api.scheduler.RoflanRunnable;
import api.storage.User;
import api.utils.MathUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Loader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RaidCommand extends Command {
    private static final String RAID =
            "[id190345817|ЮДЖИН]\n[id323998691|БОГАРДО]\n[id310289867|ЗАВР]\n[id199686399|ЛЕХА]\n".repeat(50);

    private static final String[] NAMES = {
            "микрозавр",
            "унтерменш",
            "Микрослав",
            "микрочел",
            "кулебяки",
            "довн"
    };

    private RaidRunnable runnable;
    public RaidCommand() {
        super("raid");
    }

    @Contract(pure = true)
    @Override
    public final void execute(User user, @NotNull String[] args) {
        if(args.length == 1) {
            try {
                StringBuilder builder = new StringBuilder();
                Loader.getVkApiClient()
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
                            );
                        })
                        .forEach(member -> builder.append("[id")
                                        .append(member.getMemberId())
                                        .append('|')
                                        .append(NAMES[MathUtils.RANDOM.nextInt(NAMES.length)])
                                        .append("]\n"));
                if(builder.length() <= 0) {
                    return;
                }
                runnable = new RaidRunnable(user, builder.toString().repeat(20));
                runnable.runTaskTimer(0, 2);
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
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
