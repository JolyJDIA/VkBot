package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
import api.utils.chat.MessageChannel;
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
                MessageChannel.editChat(Bot.getConfig().getProperty("defaultTitleChat"), sender.getPeerId());
                return;
            }
            MessageChannel.editChat(StringBind.toString(args), sender.getPeerId());
        }
    }
}