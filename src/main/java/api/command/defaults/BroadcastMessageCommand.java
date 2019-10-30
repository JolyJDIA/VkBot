package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
import org.jetbrains.annotations.NotNull;

public class BroadcastMessageCommand extends Command {
    private static final int ADMIN = 310289867;
    public BroadcastMessageCommand() {
        super("broadcastMessage");
        setAlias("globalMsg");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length > 1) {
            if(sender.getUserId() == ADMIN) {
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
