package api.command.defaults;

import api.command.Command;
import api.storage.User;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TickPerSecondCommand extends Command {

    public TickPerSecondCommand() {
        super("gc");
        setAlias("lag", "tps");

    }
    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            StringBuilder builder = new StringBuilder(
                    """
                    tps за последние:
                    1м | 5м | 15м:
                    """);
            for (double tps : Bot.getScheduler().getAverageTPS()) {
                builder.append(format(tps)).append(", ");
            }
            sender.sendMessage(
                    builder.substring(0, builder.length()-2) +
                    "\n-------------------------------------"+
                    "\nВся память: " + humanReadableByteCount(totalMemory) +
                    "\nСъедено памяти: " + humanReadableByteCount((totalMemory - freeMemory)) +
                    "\nСвободно памяти: " + humanReadableByteCount(freeMemory));
        }
    }
    @NonNls
    private static @NotNull String format(double tps) {
        return ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
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