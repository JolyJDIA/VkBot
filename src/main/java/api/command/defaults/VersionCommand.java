package api.command.defaults;

import api.command.Command;
import api.entity.User;
import jolyjdia.bot.Loader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("ver");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.sendMessageFromHisChat(
                    "VK API: " + Loader.getVkApiClient().getVersion() +
                    "\nВерсия бота: v1.0.7");
        }
    }
}
