package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;
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
                String text = StringBind.toString(args);
                for(int id : Bot.getProfileList().getChats()) {
                    ObedientBot.sendMessage(text, id);
                }
            } else {
                ObedientBot.sendMessage("У вас нет прав", sender.getPeerId());
            }
        }
    }
}
