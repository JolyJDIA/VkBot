package jolyjdia.bot.commands;

import api.JavaModule;
import api.command.RegisterCommandList;

public class CommandRegister extends JavaModule {
    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new HelpCommands());
        RegisterCommandList.registerCommand(new UpTimeCommand());
        RegisterCommandList.registerCommand(new EditTitleChatCommand());
        RegisterCommandList.registerCommand(new RankCommand());
    }
}
