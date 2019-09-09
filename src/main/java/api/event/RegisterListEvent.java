package api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractQueue;
import java.util.PriorityQueue;

public final class RegisterListEvent {
    private static final AbstractQueue<Handler> handlers = new PriorityQueue<>((o1, o2) -> o2.compareTo(o1.priority));

    @Contract(pure = true)
    private RegisterListEvent() {}

    public static void registerEvent(@NotNull Listener listener) {
        for(Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(EventHandler.class)) {
                continue;
            }
            Class<?> parameter = method.getParameterTypes()[0];
            if(!Event.class.isAssignableFrom(parameter)) {
                continue;
            }
            handlers.add(new Handler(event -> {
                if(!event.getClass().isAssignableFrom(parameter)) {
                    return;
                }
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }, method.getAnnotation(EventHandler.class).priority()));
        }
    }

    @Contract(pure = true)
    public static Iterable<Handler> getHandlers() {
        return handlers;
    }
}