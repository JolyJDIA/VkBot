package jolyjdia.bot.game;

import api.command.Command;
import api.entity.User;
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
            ObedientBot.sendMessage(String.valueOf(rnd(null, args[1])), sender.getPeerId());
        } else if(args.length == 3) {
            ObedientBot.sendMessage(String.valueOf(rnd(args[2], args[1])), sender.getPeerId());
        }
    }
    private static int rnd(String min, String max) {
        int maxI = 100;
        int minI = 1;
        try {
            maxI = Integer.parseInt(max);
            minI = Integer.parseInt(min);
        } catch (NumberFormatException ignored) {}
        maxI -= minI;
        ++maxI;
        return (int) (Math.random() * maxI) + minI;
    }
}
