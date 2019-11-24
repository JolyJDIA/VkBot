package api.command.defaults;

import api.command.Command;
import api.storage.User;
import jolyjdia.bot.Bot;
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
            if(noPermission(sender)) {
                return;
            }
            Bot.getModuleLoader().reloadModule();
            sender.sendMessage("Перезагрузка завершена\nМой планировщик: Привет из Thread-2");
        }
    }
}
