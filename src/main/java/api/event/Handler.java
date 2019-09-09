package api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class Handler implements Comparable<EventPriority>, Consumer<Event> {
    public final Consumer<Event> c;
    final EventPriority priority;

    @Contract(pure = true)
    Handler(Consumer<Event> consumer, EventPriority priority) {
        this.c = consumer;
        this.priority = priority;
    }

    @Override
    public final void accept(Event t) {
        c.accept(t);
    }

    @Override
    public final int compareTo(@NotNull EventPriority handler) {
        return priority.getSlot() - handler.getSlot();
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Handler)) {
            return false;
        }
        Handler other = (Handler) obj;
        return Objects.equals(c, other.c) && priority == other.priority;
    }

    @Contract(pure = true)
    @Override
    public final int hashCode() {
        return priority.getSlot();
    }
}
