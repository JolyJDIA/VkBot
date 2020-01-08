package jolyjdia.bot.utils;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class FlipCommand extends Command {
    FlipCommand() {
        super("flip", "орел или решка");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.getChat().sendMessage(MathUtils.RANDOM.nextBoolean() ? "Орел" : "Решка");
        }
    }
}
