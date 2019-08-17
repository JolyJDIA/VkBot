package jolyjdia.bot.commands;

import api.command.Command;
import api.command.RegisterCommandList;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class HelpCommands extends Command {
    public HelpCommands() {
        super("/info | info math",
                "просмотреть информацию"
        );
        setAlias("info", "help", "?", "cmds");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            StringBuilder builder = new StringBuilder();
            RegisterCommandList.getRegisteredCommands().stream()
                    .filter(cmd -> !(cmd.getUsageMessage().isEmpty() && cmd.getDescription().isEmpty()))
                    .forEach(cmd -> builder
                            .append(cmd.getUsageMessage())
                            .append(" - ")
                            .append(cmd.getDescription())
                            .append('\n'));
            ObedientBot.sendMessage(builder.toString(), sender.getPeerId());
        } else if (args.length == 2 && args[1].equalsIgnoreCase("math")) {
            ObedientBot.sendMessage("Потом", sender.getPeerId());
        } else {
            ObedientBot.sendMessage("Использование: " + getUsageMessage(), sender.getPeerId());
        }
    }
}
