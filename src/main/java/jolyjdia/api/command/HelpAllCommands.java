package jolyjdia.api.command;

import jolyjdia.bot.Bot;

public class HelpAllCommands {
    private String helpCommand;
    private String helpMath;

    public final void initializeHelp() {
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
        this.helpCommand = builder.toString();
        builder.setLength(0);
        /*for (String s : MathFunctions.ADV_OPERATOR_LIST) {//РЕФЛЕКСИЯ
            builder.append(s).append("(число)\n");
        }*/
        this.helpMath = builder.toString();
    }

    public final String getHelpCommand() {
        return helpCommand;
    }

    public final String getHelpMath() {
        return helpMath;
    }
}
