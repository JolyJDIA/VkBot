package jolyjdia.api.command.defaults;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.Chat;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class InfoUserCommand extends Command {
    public InfoUserCommand() {
        super("userinfo", "<пользователь>", "отображает информацию о пользователе");
        setAlias("infouser");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        Chat<?> chat = sender.getChat();
        if(args.length == 1) {
            chat.sendMessage(sender.toString());
        } else if(args.length == 2) {
            VkUtils.getUserId(args[1]).ifPresentOrElse(id -> {
                User target = Bot.getUserBackend().addIfAbsentAndReturnUser(chat.getPeerId(), id);
                chat.sendMessage(target.toString());
            }, () -> chat.sendMessage("Не удалось найти этого пользователя в базе"));
        }
    }
}
