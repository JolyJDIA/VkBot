package jolyjdia.bot.password;

import api.command.RegisterCommandList;
import api.utils.JavaModule;

public class GeneratorPassword extends JavaModule {

    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new PasswordCommand());
    }
}
