package api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class Handler implements Comparable<EventPriority>, Consumer<Event> {
    public final Consumer<Event> consumer;
    final EventPriority priority;

    @Contract(pure = true)
    Handler(Consumer<Event> consumer, EventPriority priority) {
        this.consumer = consumer;
        this.priority = priority;
    }

    @Override
    public final void accept(Event t) {
        consumer.accept(t);
    }

    @Override
    public final int compareTo(@NotNull EventPriority handler) {
        return priority.getSlot() - handler.getSlot();
    }

    @Contract(pure = true)
    @Override
    public final int hashCode() {
        return priority.getSlot();
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Handler handler = (Handler) o;
        return Objects.equals(consumer, handler.consumer) && priority == handler.priority;
    }
}
