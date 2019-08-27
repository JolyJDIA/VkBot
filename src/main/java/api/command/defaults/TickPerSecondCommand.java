package api.command.defaults;

import api.TickPerSeconds;
import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TickPerSecondCommand extends Command {
    public TickPerSecondCommand() {
        super("gc", "[NEW] производительность");
        setAlias("lag", "tps");
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length == 1) {
            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            @NonNls String sb =
                    "TPS: " + TickPerSeconds.getAverageTPS() +
                    "\nВся память: " + humanReadableByteCount(totalMemory) +
                    "\nСъедено памяти  : " + humanReadableByteCount((totalMemory - freeMemory)) +
                    "\nСвободно памяти: " + humanReadableByteCount(freeMemory);
            ObedientBot.sendMessage(sb, sender.getPeerId());

        }
    }
    private static String humanReadableByteCount(long bytes) {
        if (bytes < 1000) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1000));
        String pre = String.valueOf("kMGTPE".charAt(exp - 1));
        return String.format("%.1f %sB", bytes / Math.pow(1000, exp), pre);
    }
}