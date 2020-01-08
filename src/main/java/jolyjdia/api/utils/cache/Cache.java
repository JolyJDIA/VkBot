package jolyjdia.api.utils.cache;

import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Cache<K, V> {
    private final ConcurrentHashMap<K, Container<V>> cache = new ConcurrentHashMap<>();
    private final RemovalListener<? super K, ? super V> removeListener;
    private final long delay;

    public Cache(@NotNull CacheBuilder<K, V> builder) {
        this.removeListener = builder.getRemovalListener();
        this.delay = builder.getDelay();
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> cache.entrySet().removeIf(e -> {
            long left = e.getValue().getDelay() - System.currentTimeMillis();
            if(left <= 0) {
                removeListener.onRemoval(new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue().get()));
                return true;
            }
            return false;
        }), builder.getTicker(), builder.getTicker());
    }

    public final V put(K key, V value) {
        cache.put(key, new Container<>(value, System.currentTimeMillis() + delay));
        return value;
    }
    public final V get(K key) {
        Container<V> element = cache.get(key);
        element.setDelay(System.currentTimeMillis() + delay);
        return element.get();
    }
    public final boolean containsKey(K key) {
        return cache.containsKey(key);
    }
    public final void remove(K key) {
        cache.remove(key);
    }
    public final Collection<V> values() {
        return cache.values().stream().map(Container::get).collect(Collectors.toSet());//TODO
    }
}
