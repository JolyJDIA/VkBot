package api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class RegisterListEvent {
    private static final Set<Consumer<Event>> handlers = new HashSet<>();

    @Contract(pure = true)
    private RegisterListEvent() {}

    public static void registerEvent(@NotNull Listener listener) {
        for(Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(EventHandler.class)) {
                continue;
            }
            if(!Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                continue;
            }
            Consumer<Event> eventConsumer = event -> {
                if(!event.getClass().isAssignableFrom(method.getParameterTypes()[0])) {
                    return;
                }
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            };
            handlers.add(eventConsumer);
        }
    }

    @Contract(pure = true)
    public static Set<Consumer<Event>> getHandlers() {
        return handlers;
    }
}