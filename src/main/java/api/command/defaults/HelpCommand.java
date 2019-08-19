package api.command.defaults;

import api.command.Command;
import api.command.RegisterCommandList;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "просмотреть информацию");
        setAlias("info", "?", "cmds");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            StringBuilder builder = new StringBuilder();
            RegisterCommandList.getRegisteredCommands().stream()
                    .filter(cmd -> cmd.getDescription() != null && !cmd.getDescription().isEmpty())
                    .forEach(cmd -> {
                                builder.append('/').append(cmd.getName()).append(' ');
                                if (cmd.getArguments() != null && !cmd.getArguments().isEmpty()) {
                                    builder.append(cmd.getArguments());
                                }
                                builder.append(" - ").append(cmd.getDescription()).append('\n');
                            });
            ObedientBot.sendMessage(builder.toString(), sender.getPeerId());
        } else if (args.length == 2 && args[1].equalsIgnoreCase("math")) {
            ObedientBot.sendMessage("Потом", sender.getPeerId());
        } else {
            ObedientBot.sendMessage("Использование: " + getArguments(), sender.getPeerId());
        }
    }
}
