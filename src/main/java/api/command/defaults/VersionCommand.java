package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("ver");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            ObedientBot.sendMessage(
                    "VK API: " +Bot.getVkApiClient().getVersion() +
                    "\nВерсия бота: v1.0.5", sender.getPeerId());
        }
    }
}
