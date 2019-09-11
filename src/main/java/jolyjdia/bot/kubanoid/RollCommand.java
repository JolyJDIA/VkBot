package jolyjdia.bot.kubanoid;

import api.command.Command;
import api.entity.User;
import api.utils.MathUtils;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RollCommand extends Command {
    RollCommand() {
        super("roll");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            ObedientBot.sendMessage(String.valueOf(new Random().nextInt(100) + 1), sender.getPeerId());
        } else if(args.length == 2) {
            ObedientBot.sendMessage(String.valueOf(MathUtils.random(null, args[1])), sender.getPeerId());
        } else if(args.length == 3) {
            ObedientBot.sendMessage(String.valueOf(MathUtils.random(args[2], args[1])), sender.getPeerId());
        }
    }
}
