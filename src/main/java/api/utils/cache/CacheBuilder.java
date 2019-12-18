package api.utils.cache;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CacheBuilder<K, V> {
    private long delay;
    private int ticker = 2;
    private RemovalListener<? super K, ? super V> removalListener;

    @Contract(value = " -> new", pure = true)
    public static @NotNull CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<>();
    }

    public final CacheBuilder<K, V> expireAfterWrite(long duration, @NotNull TimeUnit unit) {
        this.delay = unit.toMillis(duration);
        return this;
    }

    public final <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removeListener(RemovalListener<K1, V1> listener) {
        this.removalListener = (RemovalListener<? super K, ? super V>) listener;
        return (CacheBuilder<K1, V1>) this;
    }

    public final CacheBuilder<K, V> ticker(int ticker) {
        this.ticker = ticker;
        return this;
    }

    @Contract(" -> new")
    public final @NotNull Cache<K, V> build() {
        return new Cache<>(this);
    }

    public final long getDelay() {
        return delay;
    }

    public final int getTicker() {
        return ticker;
    }

    public final RemovalListener<? super K, ? super V> getRemovalListener() {
        return removalListener;
    }
}