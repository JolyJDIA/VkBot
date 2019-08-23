package api.command.defaults;

import api.JavaModule;
import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload", "перезагрузка");
        setAlias("rl");
        setPermission("roflanbot.reload", "У вас нет прав");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length == 1) {
            JavaModule.reloadModule();
            ObedientBot.sendMessage("Перезагрузка завершена", sender.getPeerId());
        }
    }
}
