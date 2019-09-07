package jolyjdia.bot.game;

import api.JavaModule;
import api.command.RegisterCommandList;
import api.event.RegisterListEvent;

public class GameLoad extends JavaModule {

    @Override
    public final void onLoad() {
        RegisterListEvent.registerEvent(new GameListener());
        RegisterCommandList.registerCommand(new GameCommand());
        RegisterCommandList.registerCommand(new RollCommand());
    }
}
