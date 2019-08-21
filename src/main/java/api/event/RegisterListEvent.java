package api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class RegisterListEvent {
    private static final Set<Listener> listeners = new HashSet<>();//Классы уникальные

    public static void registerEvent(Listener listener) {
        listeners.add(listener);
    }

    @Contract(pure = true)
    @NotNull
    public static Iterable<Listener> getRegisteredListeners() {
        return listeners;
    }
    public static void unregisterAll() {
        listeners.clear();
    }
    public static void registerAll(@NotNull Iterable<? extends Listener> listeners) {
        listeners.forEach(RegisterListEvent::registerEvent);
    }
}