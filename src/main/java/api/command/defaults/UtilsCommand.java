package api.command.defaults;

import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class UtilsCommand extends Command {
    public UtilsCommand() {
        super("utils", "команды-утилиты");
        setAlias("uptime", "calendar");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if(args.length != 1) {
            return;
        }
        switch (args[0]) {
            case "utils" -> sender.sendMessageFromChat(String.valueOf(getAlias()));
            case "uptime" -> {
                RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
                long uptime = mxBean.getUptime();

                @NonNls String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(uptime),
                        TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime)),
                        TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime)));
                sender.sendMessageFromChat("Время работы " + hms);
            }
            case "calendar" -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Время: hhч mmм ssс\nДата: dd.MM.yyyy");
                sender.sendMessageFromChat(formatter.format(LocalDateTime.now()));
            }
        }
    }
}
