package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.entity.User;
import api.utils.StringBind;
import jolyjdia.bot.Loader;
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
                Bot.editChat(Loader.getConfig().getProperty("defaultTitleChat"), sender.getPeerId());
                return;
            }
            Bot.editChat(StringBind.toString(args), sender.getPeerId());
        }
    }
}