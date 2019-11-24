package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.VkUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class InfoUserCommand extends Command {
    public InfoUserCommand() {
        super("userinfo", "<пользователь>", "отображает информацию о пользователе");
        setAlias("infouser");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.sendMessageFromChat(sender.toString());
        } else if(args.length == 2) {
            VkUtils.getUserId(args[1]).ifPresentOrElse(id -> {
                User target = Bot.getUserBackend().addIfAbsentAndReturn(sender.getPeerId(), id);
                sender.sendMessageFromChat(target.toString());
            }, () -> sender.sendMessageFromChat("Не удалось найти этого пользователя в базе"));
        }
    }
}
