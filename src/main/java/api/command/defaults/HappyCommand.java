package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.timeformat.TemporalDuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class HappyCommand extends Command {
    public static final String NEW_YEAR = "\uD83D\uDD25\uD83E\uDD76Новый Год через: %s\uD83E\uDD76\uD83D\uDD25";
    public HappyCommand() {
        super("др");
        setAlias("нг");
    }
    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("др")) {
                @NonNls String date = TemporalDuration.of(10, 12, 0,0).toString();
                sender.getChat().sendMessage("\uD83D\uDD25ДР-ROFLANBOAT\uD83D\uDD25 через: "+date + "\uD83D\uDD25");
            } else if(args[0].equalsIgnoreCase("нг")) {
                sender.getChat().sendMessage(String.format(NEW_YEAR, TemporalDuration.of(1, 1, 0,0)));
            }
        }
    }
}