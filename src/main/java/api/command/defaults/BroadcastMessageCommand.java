package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
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
            if(STAFF_ADMIN.containsValue(sender.getUserId())) {
                sender.sendMessageFromChat("Объявление отправится только активным беседам!");
                String text = StringBind.toString(args);
                Bot.getUserBackend().getChats().forEach(id -> Bot.sendMessage(text, id));
            } else {
                sender.sendMessageFromChat("У вас нет прав");
            }
        }
    }
}
