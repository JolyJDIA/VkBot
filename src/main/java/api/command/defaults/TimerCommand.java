package api.command.defaults;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NotNull;

public class TimerCommand extends Command {

    public TimerCommand() {
        super("timer");
    }

    @Override
    public void execute(User sender, @NotNull String[] args) {
        if(args.length == 2) {
            System.out.println(sender.getPeerId());
            ObedientBot.SCHEDULER.scheduleSyncDelayTask(new Runnable() {
                @Override
                public void run() {
                    ObedientBot.sendMessage("ВСЕ", sender.getPeerId());
                }
            }, Integer.parseInt(args[1]));
        }
    }
}
