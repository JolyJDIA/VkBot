package jolyjdia.api.utils.timeformat;

import java.time.Duration;

public enum TimeFormatter {
    DAYS(Duration::toDays, " дней ", " день ", " дня "),
    HOURS(duration -> duration.toHours() % 24, " часов ", " час ", " часа "),
    MINUTES(duration -> duration.toMinutes() % 60, " минут ", " минута ", " минуты "),
    SECONDS(duration -> duration.toSeconds() % 60, " секунд ", " секунда ", " секунды ");

    private final CallableDuration callable;
    private final String plural, singular, other;

    TimeFormatter(CallableDuration callable, String plural, String singular, String other) {
        this.callable = callable;
        this.plural = plural;
        this.singular = singular;
        this.other = other;
    }

    public CallableDuration getCallable() {
        return callable;
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }

    public String getOther() {
        return other;
    }
}