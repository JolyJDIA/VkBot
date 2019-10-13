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
            sender.sendMessageFromHisChat(String.valueOf(MathUtils.RANDOM.nextInt(100) + 1));
        }
        if(args.length >= 2 && args.length < 4) {
            try {
                int a = Integer.parseInt(args[1]);
                if(a < 0) {
                    sender.sendMessageFromHisChat("Минимальное число должно быть положительным");
                    return;
                }
                if (args.length == 2) {
                    sender.sendMessageFromHisChat(String.valueOf(MathUtils.random(a)));
                } else {
                    int max = Integer.parseInt(args[2]);
                    if(max < a) {
                        sender.sendMessageFromHisChat("Максимальное число должно быть больше минимального");
                        return;
                    }
                    sender.sendMessageFromHisChat(String.valueOf(MathUtils.random(a, max)));
                }
            } catch (NumberFormatException e) {
                sender.sendMessageFromHisChat("Это не число!");
            }
        }
    }
}
