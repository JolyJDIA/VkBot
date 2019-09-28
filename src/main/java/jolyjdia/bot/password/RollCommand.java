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
        } else if(args.length == 2) {
            sender.sendMessageFromHisChat(String.valueOf(MathUtils.random(null, args[1])));
        } else if(args.length == 3) {
            sender.sendMessageFromHisChat(String.valueOf(MathUtils.random(args[2], args[1])));
        }
    }
}
