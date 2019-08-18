package api.event;

import org.jetbrains.annotations.NotNull;

public class Event {
    private String name;
    @NotNull
    public final String getEventName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }
        return name;
    }
}
