package jolyjdia.bot.raid;

import api.command.RegisterCommandList;
import api.utils.JavaModule;

public class Raid extends JavaModule {

    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new RaidCommand());
    }
}
