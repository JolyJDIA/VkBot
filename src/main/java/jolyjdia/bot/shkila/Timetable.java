package jolyjdia.bot.shkila;

import api.JavaModule;
import api.command.RegisterCommandList;
import org.jetbrains.annotations.NonNls;

public class Timetable extends JavaModule {
    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new TimetableCommand());
    }
    @NonNls static final String TIMETABLE =
            "ВТОРНИК-ПЯТНИЦА\n"+
            "1.урок - 8:00 - 8:45\n"+
            "2.урок - 8:55 - 9:40\n"+
            "3.урок - 9:55 - 10:40\n"+
            "4.урок - 10:55 - 11:40\n"+
            "5.урок - 11:50 - 12:35\n"+
            "6.урок - 12:40 - 13:25\n"+
            "7.урок - 13:30 - 14:15\n"+
            "ПОНЕДЕЛЬНИК\n"+
            "Класс.час - 8:00 - 8:30\n"+
            "1.урок - 8:40 - 9:20\n"+
            "2.урок - 9:30 - 10:10\n"+
            "3.урок - 10:25 - 11:05\n"+
            "4.урок - 11:15 - 11:55\n"+
            "5.урок - 12:00 - 12:40\n"+
            "6.урок - 12:45 - 13:25\n"+
            "7.урок - 13:30 - 14:10";
}
