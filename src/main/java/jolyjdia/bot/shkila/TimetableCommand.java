package jolyjdia.bot.shkila;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import org.jetbrains.annotations.NotNull;

public class TimetableCommand extends Command {
    TimetableCommand() {
        super("расписание");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            ObedientBot.sendMessage(Timetable.TIMETABLE, sender.getPeerId());
        }
    }
}
