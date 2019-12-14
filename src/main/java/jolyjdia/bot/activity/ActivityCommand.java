package jolyjdia.bot.activity;

import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.NotNull;

public class ActivityCommand extends Command {
    private final ActivityLoad activity;
    protected ActivityCommand(ActivityLoad activity) {
        super("online");
        setAlias("offline");
        this.activity = activity;
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length != 1) {
            return;
        }
        if(args[0].equalsIgnoreCase("online")) {
            activity.setOffline(false);
        } else if(args[0].equalsIgnoreCase("offline")) {
            activity.setOffline(true);
        }
        sender.sendMessage("Вы установили статус: "+activity.getOnlineFormat());
    }
}
