package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class BroadcastMessageCommand extends Command {
    public static final int ZAVR = 310289867;
    public BroadcastMessageCommand() {
        super("broadcastMessage");
        setAlias("globalMsg");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length > 1) {
            if(sender.getUserId() == ZAVR) {
                sender.sendMessageFromChat("Объявление отправится только активным беседам!");
                String text = StringBind.toString(args);
                for(int id : Bot.getUserBackend().getChats()) {
                    Bot.sendMessage(text, id);
                }
            } else {
                sender.sendMessageFromChat("У вас нет прав");
            }
        }
    }
}
