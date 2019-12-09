package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.VkUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload", "перезагрузка");
        setAlias("rl", "stop");
        setPermission("roflanbot.reload", "У вас нет прав");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length == 1) {
            if (noPermission(sender)) {
                return;
            }
            if(args[0].equalsIgnoreCase("stop")) {
                try {
                    Bot.getVkApiClient().status()
                            .set(VkUtils.USER_ACTOR)
                            .text("Осторожно! Злой динозаврик!")
                            .execute();
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }
                System.exit(-1);
            } else {
                Bot.getModuleLoader().reloadModule();
                sender.sendMessage("Перезагрузка завершена\nМой планировщик: Привет из Thread-2");
            }
        }
    }
}
