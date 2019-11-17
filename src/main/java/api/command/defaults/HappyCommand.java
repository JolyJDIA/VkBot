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
    public static final String HAPPY =
            "\uD83D\uDC9E\uD83C\uDF7A\uD83D\uDD25Happy Birthday RoflanBoat\uD83D\uDC9E\uD83C\uDF7A\uD83D\uDD25";

    public static final String TO = "\uD83D\uDD25ДР-ROFLANBOAT ЧЕРЕЗ : %s \uD83D\uDD25";

    public HappyCommand() {
        super("др");
        setAlias("нг");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("др")) {
                sender.sendMessageFromChat(String.format(TO, getFormat(10, 12)));
            } else if(args[0].equalsIgnoreCase("нг")) {
                sender.sendMessageFromChat("❄Новый Год через: "+getFormat(1, 1)+ '❄');
            }
        }
    }
    @NonNls
    private static @NotNull String getFormat(int month, int day) {
        LocalDateTime baseDate = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.of(getYear(month, day), month, day, 0, 0);
        long diff = newDate.toInstant(ZoneOffset.UTC).toEpochMilli() - baseDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
        return toFormat(days, TimeFormatter.DAYS) + ' ' +
                toFormat(hours, TimeFormatter.HOURS) + ' ' +
                toFormat(minutes, TimeFormatter.MINUTES) + ' ' +
                toFormat(seconds, TimeFormatter.SECONDS);
    }
    @NonNls
    @Contract(pure = true)
    public static @NotNull String toFormat(long num, TimeFormatter formatter) {
        long preLastDigit = num % 100 / 10;
        if (preLastDigit == 1) {
            return num+" "+formatter.getDeclination()[0];
        }
        return num+" "+switch ((int) (num % 10)) {
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
    private static int getYear(int month, int days) {
        LocalDateTime baseDate = LocalDateTime.now();
        return month < baseDate.getMonthValue() || days < baseDate.getDayOfMonth() ? baseDate.getYear()+1 : baseDate.getYear();
    }
}
