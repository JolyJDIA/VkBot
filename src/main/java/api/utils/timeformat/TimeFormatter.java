package api.utils.timeformat;

import org.jetbrains.annotations.Contract;

import java.time.Duration;

public enum TimeFormatter {
    DAYS(Duration::toDays, " дней ", " день ", " дня "),
    HOURS(duration -> duration.toHours() % 24, " часов ", " час ", " часа "),
    MINUTES(duration -> duration.toMinutes() % 60, " минут ", " минута ", " минуты "),
    SECONDS(duration -> duration.toSeconds() % 60, " секунд ", " секунда ", " секунды ");
    private final CallebleDuration callable;
    private final String plural, singular, other;
    @Contract(pure = true)
    TimeFormatter(CallebleDuration callable, String plural, String singular, String other) {
        this.callable = callable;
        this.plural = plural;
        this.singular = singular;
        this.other = other;
    }
    @Contract(pure = true)
    public CallebleDuration getCallable() {
        return callable;
    }
    @Contract(pure = true)
    public String getPlural() {
        return plural;
    }
    @Contract(pure = true)
    public String getSingular() {
        return singular;
    }
    @Contract(pure = true)
    public String getOther() {
        return other;
    }
}