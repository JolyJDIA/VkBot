package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class BroadcastMessages extends Command {

    public BroadcastMessages() {
        super("broadcastMessage", "<Сообещние>", "отправить сообщение всем беседам");
        setAlias("globalMsg");
        setPermission("roflanbot.broadcastMessage", "У вас нет прав");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length > 1) {
            if (noPermission(sender)) {
                return;
            }
            String text = StringBind.toString(args);
            for(int id : Bot.getProfileList().getChats()) {
                ObedientBot.sendMessage(text, id);
            }
        }
    }
}
