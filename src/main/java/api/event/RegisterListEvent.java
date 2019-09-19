package api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class RegisterListEvent {
    private final List<Handler> handlers = new ArrayList<>();

    public final void registerEvent(@NotNull Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (!method.isAnnotationPresent(EventLabel.class)) {
                continue;
            }
            Class<?> parameter = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(parameter)) {
                continue;
            }
            handlers.add(new Handler(event -> {
                if (!event.getClass().isAssignableFrom(parameter)) {
                    return;
                }
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }, method.getAnnotation(EventLabel.class).priority()));
        }
        handlers.sort((o1, o2) -> o2.compareTo(o1.priority));
    }

    @Contract(pure = true)
    public final List<Handler> getHandlers() {
        return handlers;
    }
    public final void unregisterAll() {
        handlers.clear();
    }

    public final void registerAll(@NotNull Iterable<? extends Listener> iterable) {
        iterable.forEach(this::registerEvent);
    }
    public static class Handler implements Comparable<EventPriority>, Consumer<Event> {
        final Consumer<? super Event> consumer;
        final EventPriority priority;

        @Contract(pure = true)
        Handler(Consumer<? super Event> consumer, EventPriority priority) {
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
}