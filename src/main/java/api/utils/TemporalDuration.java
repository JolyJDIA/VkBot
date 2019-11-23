package api.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TemporalDuration {
    private static final TimeFormatter[] DEFAULT_FORMAT = {TimeFormatter.DAYS, TimeFormatter.HOURS, TimeFormatter.MINUTES, TimeFormatter.SECONDS};

    //23 11 -> 24 11 - не прибавляю
    //23 11 -> 20 11 - прибавляю
    public static @NotNull TemporalDuration of(int month, int day, int hour, int minute) {
        LocalDateTime baseDate = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.of(baseDate.getYear(), month, day, hour, minute);
        if(baseDate.toInstant(ZoneOffset.UTC).toEpochMilli() > newDate.toInstant(ZoneOffset.UTC).toEpochMilli()) {
            newDate = newDate.plusYears(1);
        }
        return new TemporalDuration(Duration.between(baseDate, newDate));
    }
    private Duration duration;

    public TemporalDuration(Duration duration) {
        if(duration == null) {
            return;
        }
        this.duration = duration;
    }

    @Override
    public final @NotNull String toString() {
        return toFormat(DEFAULT_FORMAT);
    }
    public final @NotNull String toFormat(@NotNull TimeFormatter... timeFormatter) {
        StringBuilder buf = new StringBuilder();
        for(TimeFormatter formatter : timeFormatter) {
            long x = formatter.getCallable().call(duration);
            long preLastDigit = x % 100 / 10;
            if (preLastDigit == 1) {
                buf.append(x).append(formatter.getDeclination()[0]);
                continue;
            }
            long y = x % 10;
            if (y == 1) {
                buf.append(x).append(formatter.getDeclination()[1]);
            } else if (y == 2 || y == 3 || y == 4) {
                buf.append(x).append(formatter.getDeclination()[2]);
            } else {
                buf.append(x).append(formatter.getDeclination()[0]);
            }
        }
        return buf.toString();
    }
    @FunctionalInterface
    private interface CallebleField {
        long call(Duration duration);
    }
    public enum TimeFormatter {
        DAYS(Duration::toDays, " дней ", " день ", " дня "),
        HOURS(Duration::toHours, " часов ", " час ", " часа "),
        MINUTES(Duration::toMinutes, " минут ", " минута ", " минуты "),
        SECONDS(Duration::toSeconds, " секунд ", " секунда ", " секунды ");
        private final String[] declination;
        private final CallebleField callable;
        @Contract(pure = true)
        TimeFormatter(CallebleField callable, String... declination) {
            this.callable = callable;
            this.declination = declination;
        }
        @Contract(pure = true)
        public String[] getDeclination() {
            return declination;
        }

        public CallebleField getCallable() {
            return callable;
        }
    }
}