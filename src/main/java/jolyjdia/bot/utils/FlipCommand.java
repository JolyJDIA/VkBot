package jolyjdia.bot.utils;

import api.command.Command;
import api.storage.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class FlipCommand extends Command {
    FlipCommand() {
        super("flip");
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.sendMessageFromChat(MathUtils.RANDOM.nextBoolean() ? "Орел" : "Решка");
        }
    }
}