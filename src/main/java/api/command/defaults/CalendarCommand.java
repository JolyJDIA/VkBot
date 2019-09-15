package api.command.defaults;

import api.command.Command;
import api.entity.User;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


public class CalendarCommand extends Command {
    public CalendarCommand() {
        super("calendar", "[NEW] дата и время");
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (args.length == 1) {
            LocalDateTime localDateTime = LocalDateTime.now();
            int hour = localDateTime.getHour();
            int minute = localDateTime.getMinute();
            int sec = localDateTime.getSecond();
            @NonNls String time = "Время: "+
                    (hour < 10 ? "0" : "")+hour + "ч " +
                    (minute < 10 ? "0" : "")+minute + "м " +
                    (sec < 10 ? "0" : "")+sec +'с';
            int day = localDateTime.getDayOfMonth();
            int month = localDateTime.getMonthValue();
            @NonNls String date = "Дата: "+
                    (day < 10 ? "0" : "")+day+ '.' +
                    (month < 10 ? "0" : "")+ month+ '.' +
                    localDateTime.getYear();
            sender.sendMessageFromHisChat(time+'\n'+date);
        }
    }
}
