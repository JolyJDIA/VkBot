package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.entity.User;
import jolyjdia.bot.calculate.calculator.MathFunctions;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "<math>", "просмотреть информацию");
        setAlias("info", "?");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length == 1) {
            StringBuilder builder = new StringBuilder();
            Bot.getBotManager().getRegisteredCommands().stream()
                    .filter(cmd -> cmd.getDescription() != null && !cmd.getDescription().isEmpty())
                    .forEach(cmd -> {
                                builder.append('/').append(cmd.getName());// /cmd - дададая | /cmd <да-да я> - описание
                                if (cmd.getArguments() != null && !cmd.getArguments().isEmpty()) {
                                    builder.append(' ').append(cmd.getArguments());
                                }
                                builder.append(" - ").append(cmd.getDescription()).append('\n');
                            });
            sender.sendMessageFromHisChat(builder.toString());
        } else if (args.length == 2) {
            if(args[1].equalsIgnoreCase("math")) {
                StringBuilder builder = new StringBuilder();
                for (String s : MathFunctions.ADV_OPERATOR_LIST) {
                    builder.append(s).append("(число)\n");
                }
                sender.sendMessageFromHisChat(builder.toString());
            }
        } else {
            sender.sendMessageFromHisChat("Использование: " + getUseCommand());
        }
    }
}
