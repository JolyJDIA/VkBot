package jolyjdia.bot.minecraft;

import api.command.Command;
import api.storage.User;

public class ServerMinecraftCommand extends Command {
    protected ServerMinecraftCommand() {
        super("server");
    }

    @Override
    public void execute(User sender, String[] args) {
        if(args.length == 1) {
        }
    }
}
