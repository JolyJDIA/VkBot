package api.command.defaults;

import api.command.Command;
import api.storage.Chat;
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
        Chat<?> chat = sender.getChat();
        if(args.length == 1) {
            chat.sendMessage(sender.toString());
        } else if(args.length == 2) {
            VkUtils.getUserId(args[1]).ifPresentOrElse(id -> {
                User target = Bot.getUserBackend().addIfAbsentAndReturn(chat.getPeerId(), id);
                chat.sendMessage(target.toString());
            }, () -> chat.sendMessage("Не удалось найти этого пользователя в базе"));
        }
    }
}
