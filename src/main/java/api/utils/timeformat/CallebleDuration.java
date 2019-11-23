package api.utils.timeformat;

import java.time.Duration;

@FunctionalInterface
public interface CallebleDuration {
    long call(Duration duration);
}