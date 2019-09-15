package jolyjdia.bot.kubanoid;

import api.command.Command;
import api.entity.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class FlipCommand extends Command {
    FlipCommand() {
        super("flip");
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.sendMessageFromHisChat(MathUtils.RANDOM.nextInt(2) == 1 ? "Орел" : "Решка");
        }
    }
}
