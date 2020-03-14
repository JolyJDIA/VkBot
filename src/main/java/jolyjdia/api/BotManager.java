package jolyjdia.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jolyjdia.api.command.Command;
import jolyjdia.api.command.defaults.*;
import jolyjdia.api.event.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class BotManager {
    private final List<Handler> listeners = Lists.newArrayList();
    private final Set<Command> commands = Sets.newHashSet();

    public BotManager() {
        registerCommand(new HelpCommand());
        registerCommand(new ReloadCommand());
        registerCommand(new RankCommand());
        registerCommand(new InfoUserCommand());
        registerCommand(new TickPerSecondCommand());
        registerCommand(new EditTitleChatCommand());
        registerCommand(new BroadcastMessageCommand());
        registerCommand(new UtilsCommand());
        registerCommand(new HappyCommand());
    }

    public void registerEvent(@NotNull Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (!method.isAnnotationPresent(EventLabel.class)) {
                continue;
            }
            Class<?> parameter = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(parameter)) {
                continue;
            }
            EventLabel label = method.getAnnotation(EventLabel.class);
            listeners.add(new Handler(event -> {
                if (!event.getClass().isAssignableFrom(parameter)) {
                    return;
                }
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }, label.priority(), label.ignoreCancelled()));
        }
        listeners.sort((o1, o2) -> o2.compareTo(o1.priority));
    }

    public List<Handler> getListeners() {
        return listeners;
    }

    public void unregisterAllEvents() {
        listeners.clear();
    }

    public void registerAllEvents(@NotNull Iterable<? extends Listener> iterable) {
        iterable.forEach(this::registerEvent);
    }
    public void registerCommand(Command command) {
        commands.add(command);
    }

    public Set<Command> getRegisteredCommands() {
        return commands;
    }

    public void unregisterAllCommands() {
        commands.clear();
    }

    public void registerAllCommands(@NotNull Iterable<? extends Command> iterable) {
        iterable.forEach(commands::add);
    }

    public static class Handler implements Comparable<EventPriority>, Consumer<Event> {
        final Consumer<? super Event> consumer;
        final EventPriority priority;
        final boolean ignoreCancelled;

        Handler(Consumer<? super Event> consumer, EventPriority priority, boolean ignoreCancelled) {
            this.consumer = consumer;
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
        }

        @Override
        public final void accept(@NotNull Event event) {
            if (Cancellable.class.isAssignableFrom(event.getClass())) {//TODO:
                if (((Cancellable) event).isCancelled() && ignoreCancelled) {
                    return;
                }
            }
            consumer.accept(event);
        }

        @Override
        public final int compareTo(@NotNull EventPriority handler) {
            return Integer.compare(priority.getSlot(), handler.getSlot());
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Handler && compareTo(((Handler) o).priority) == 0;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + (consumer == null ? 0 : consumer.hashCode());
            result = 31 * result + (priority == null ? 0 : priority.getSlot());
            result = 31 * result + (ignoreCancelled ? 1 : 0);
            return result;
        }
    }
}