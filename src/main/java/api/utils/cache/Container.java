package api.utils.cache;

public class Container<T> {
    private final T t;
    private long delay;
    public Container(T t, long delay) {
        this.delay = delay;
        this.t = t;
    }

    public final long getDelay() {
        return delay;
    }

    public final T get() {
        return t;
    }

    public final void setDelay(long delay) {
        this.delay = delay;
    }
}
