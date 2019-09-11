package jolyjdia.bot.kubanoid;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NotNull;

public class FlipCommand extends Command {
    FlipCommand() {
        super("flip");
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            ObedientBot.sendMessage(ObedientBot.RANDOM.nextInt(2) == 1 ? "Орел" : "Решка", sender.getPeerId());
        }
    }
}
