package jolyjdia.bot.password;

import api.command.Command;
import api.storage.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class RollCommand extends Command {
    RollCommand() {
        super("roll");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.sendMessageFromChat(String.valueOf(MathUtils.RANDOM.nextInt(100) + 1));
        }
        if(args.length >= 2 && args.length < 4) {
            try {
                int start = Integer.parseInt(args[1]);
                if (args.length == 2) {
                    sender.sendMessageFromChat(String.valueOf(MathUtils.random(start)));
                } else {
                    int end = Integer.parseInt(args[2]);
                    if(end < start) {
                        sender.sendMessageFromChat("Максимальное число должно быть больше минимального");
                        return;
                    }
                    sender.sendMessageFromChat(String.valueOf(MathUtils.random(start, end)));
                }
            } catch (NumberFormatException e) {
                sender.sendMessageFromChat("Это не число!");
            }
        }
    }
}
