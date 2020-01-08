package jolyjdia.api.command.defaults;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import jolyjdia.api.utils.chat.MessageChannel;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class BroadcastMessageCommand extends Command {
    public BroadcastMessageCommand() {
        super("broadcast");
        setAlias("ad");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length > 1) {
            if(sender.isStaff()) {
                sender.getChat().sendMessage("Объявление отправится только активным беседам!");
                String text = StringBind.toString(args);
                Bot.getUserBackend().getChats().forEach(id -> MessageChannel.sendMessage(text, id));
            } else {
                sender.getChat().sendMessage("У вас нет прав");
            }
        }
    }
}
