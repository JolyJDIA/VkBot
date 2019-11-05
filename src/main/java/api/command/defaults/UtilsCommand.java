package api.command.defaults;

import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class UtilsCommand extends Command {
    public UtilsCommand() {
        super("utils", "команды-утилиты");
        setAlias("uptime", "calendar", "convert_ts", "current_ts");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        switch (args[0]) {
            case "utils" -> {
                if(args.length != 1) {
                    return;
                }
                sender.sendMessageFromChat(String.valueOf(getAlias()));
            }
            case "uptime" -> {
                if(args.length != 1) {
                    return;
                }
                RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
                long uptime = mxBean.getUptime();

                @NonNls String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(uptime),
                        TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime)),
                        TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime)));
                sender.sendMessageFromChat("Время работы " + hms);
            }
            case "calendar" -> {
                if(args.length != 1) {
                    return;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Время: hhч mmм ssс\nДата: dd.MM.yyyy");
                sender.sendMessageFromChat(formatter.format(LocalDateTime.now()));
            }
            case "convert_ts" -> {
                if(args.length != 2) {
                    return;
                }
                try {
                    long millis = Long.parseLong(args[1]);
                    LocalDateTime ofInstant = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
                    sender.sendMessageFromChat(String.valueOf(ofInstant));
                } catch (NumberFormatException e) {
                    sender.sendMessageFromChat("Это не число(long)");
                }
            }
            case "current_ts" -> {
                if(args.length != 1) {
                    return;
                }
                sender.sendMessageFromChat(String.valueOf(ZonedDateTime.now().toInstant().toEpochMilli()));
            }
        }
    }
}
