package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.VkUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;

public class HappyCommand extends Command {
    public HappyCommand() {
        super("др");
        setAlias("нг");
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .text("❄Новый Год через: "+getFormatStatus(1, 1)+ '❄')
                        .execute();
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .groupId(Bot.getGroupId())
                        .text("❄Новый Год через: "+getFormatStatus(1, 1)+ '❄')
                        .execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 2000);
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().account().setOnline(VkUtils.USER_ACTOR).voip(false).execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 6000);
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
    private static @NotNull String getFormatStatus(int month, int day) {
        LocalDateTime baseDate = LocalDateTime.now();
        int year = month < baseDate.getMonthValue() || day < baseDate.getDayOfMonth() ? baseDate.getYear()+1 : baseDate.getYear();
        LocalDateTime newDate = LocalDateTime.of(year, month, day, 0, 0);
        Duration duration = Duration.between(baseDate, newDate);
        return toFormat(duration.toDays(), TimeFormatter.DAYS) + ' ' +
                toFormat(duration.toHours() % 24, TimeFormatter.HOURS) + ' ' +
                toFormat(duration.toMinutes() % 60, TimeFormatter.MINUTES);
    }
    @NonNls
    private static @NotNull String getFormat(int month, int day) {
        LocalDateTime baseDate = LocalDateTime.now();
        int year = month < baseDate.getMonthValue() || day < baseDate.getDayOfMonth() ? baseDate.getYear()+1 : baseDate.getYear();
        LocalDateTime newDate = LocalDateTime.of(year, month, day, 0, 0);
        Duration duration = Duration.between(baseDate, newDate);

        return toFormat(duration.toDays(), TimeFormatter.DAYS) + ' ' +
                toFormat(duration.toHours() % 24, TimeFormatter.HOURS) + ' ' +
                toFormat(duration.toMinutes() % 60, TimeFormatter.MINUTES) + ' ' +
                toFormat(duration.toSeconds() % 60, TimeFormatter.SECONDS);
    }
    @NonNls
    @Contract(pure = true)
    private static @NotNull String toFormat(long x, TimeFormatter formatter) {
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