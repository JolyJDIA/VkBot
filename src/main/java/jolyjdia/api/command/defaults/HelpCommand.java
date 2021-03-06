package jolyjdia.api.command.defaults;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.User;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "<math>", "просмотреть информацию");
        setAlias("info", "?");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            sender.getChat().sendMessage(Bot.getHelpCommand().getHelpCommand());
        } else if (args.length == 2) {
            if(args[1].equalsIgnoreCase("math")) {
                sender.getChat().sendMessage(Bot.getHelpCommand().getHelpMath());
            }
        } else {
            sender.getChat().sendMessage("Использование: " + getUseCommand());
        }
    }
}
