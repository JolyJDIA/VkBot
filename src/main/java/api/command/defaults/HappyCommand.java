package api.command.defaults;

import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public class HappyCommand extends Command {
    public HappyCommand() {
        super("др");
        setAlias("нг");
    }
    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("др")) {
                sender.sendMessageFromChat("\uD83D\uDD25ДР-ROFLANBOAT\uD83D\uDD25 ЧЕРЕЗ : "+getFormat(10, 12));
            } else if(args[0].equalsIgnoreCase("нг")) {
                sender.sendMessageFromChat("❄Новый Год через: "+getFormat(1, 1)+ '❄');
            }
        }
    }
    @NonNls
    private static @NotNull String getFormat(int month, int day) {
        LocalDateTime baseDate = LocalDateTime.now();
        int year = month < baseDate.getMonthValue() || day < baseDate.getDayOfMonth() ? baseDate.getYear()+1 : baseDate.getYear();
        LocalDateTime newDate = LocalDateTime.of(year, month, day, 0, 0);
        long diff = newDate.toInstant(ZoneOffset.UTC).toEpochMilli() - baseDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        return toFormat(TimeUnit.MILLISECONDS.toDays(diff), TimeFormatter.DAYS) + ' ' +
                toFormat(TimeUnit.MILLISECONDS.toHours(diff) % 24, TimeFormatter.HOURS) + ' ' +
                toFormat(TimeUnit.MILLISECONDS.toMinutes(diff) % 60, TimeFormatter.MINUTES) + ' ' +
                toFormat(TimeUnit.MILLISECONDS.toSeconds(diff) % 60, TimeFormatter.SECONDS);
    }
    @NonNls
    @Contract(pure = true)
    public static @NotNull String toFormat(long x, TimeFormatter formatter) {
        long preLastDigit = x % 100 / 10;
        if (preLastDigit == 1) {
            return x+" "+formatter.getDeclination()[0];
        }
        return x+" "+switch ((int) (x % 10)) {
            case 1 -> formatter.getDeclination()[1];
            case 2, 3, 4 -> formatter.getDeclination()[2];
            default -> formatter.getDeclination()[0];
        };
    }
    public enum TimeFormatter {
        DAYS("дней", "день", "дня"),
        HOURS("часов", "час", "часа"),
        MINUTES("минут", "минута", "минуты"),
        SECONDS("секунд", "секунда", "секунды");
        private final String[] declination;
        @Contract(pure = true)
        TimeFormatter(String... declination) {
            this.declination = declination;
        }

        @Contract(pure = true)
        public String[] getDeclination() {
            return declination;
        }
    }
}