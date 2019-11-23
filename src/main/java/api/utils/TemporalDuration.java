package api.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static java.time.temporal.ChronoField.*;

public class TemporalDuration {
    private static final TimeFormatter[] date = {TimeFormatter.DAYS, TimeFormatter.HOURS, TimeFormatter.MINUTES, TimeFormatter.SECONDS};

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
        return toFormat(date);
    }
    public final @NotNull String toFormat(@NotNull TimeFormatter... timeFormatter) {
        StringBuilder buf = new StringBuilder();
        for(TimeFormatter formatter : timeFormatter) {
            long x = formatter.getLong(duration);
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
    public enum TimeFormatter {
        YEARS(YEAR, " лет ", " год ", " года "),
        MONTHS(MONTH_OF_YEAR, " месяцев ", " месяц ", " месяца "),
        DAYS(DAY_OF_YEAR, " дней ", " день ", " дня "),
        HOURS(HOUR_OF_DAY, " часов ", " час ", " часа "),
        MINUTES(MINUTE_OF_HOUR, " минут ", " минута ", " минуты "),
        SECONDS(SECOND_OF_MINUTE, " секунд ", " секунда ", " секунды ");
        private final String[] declination;
        private final ChronoField field;
        @Contract(pure = true)
        TimeFormatter(ChronoField field, String... declination) {
            this.declination = declination;
            this.field = field;
        }
        @Contract(pure = true)
        public String[] getDeclination() {
            return declination;
        }

        @Contract(pure = true)
        public ChronoField getField() {
            return field;
        }
        private final long getLong(Duration duration) {
            switch (field) {
                case DAY_OF_YEAR -> {
                    return duration.toDays();
                }
                case HOUR_OF_DAY -> {
                    return duration.toHours() % 24;
                }
                case MINUTE_OF_HOUR -> {
                    return duration.toMinutes() % 60;
                }
                case SECOND_OF_MINUTE -> {
                    return duration.toSeconds() % 60;
                }
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
    }
}