package jolyjdia.api.command.defaults;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class EditTitleChatCommand extends Command {
    public EditTitleChatCommand() {
        super("settitle",
                "<название>", "изменить название беседы");
        setAlias("edittitle");
        setPermission("roflanbot.settitle", "У вас нет прав");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length >= 1) {
            if (noPermission(sender)) {
                return;
            }
            if (args.length == 1) {
                sender.getChat().editChat(Bot.getConfig().getProperty("defaultTitleChat"));
                return;
            }
            sender.getChat().editChat(StringBind.toString(args));
        }
    }
}