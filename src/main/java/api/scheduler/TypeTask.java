package api.scheduler;

@FunctionalInterface
public interface TypeTask {
    //invert
    default boolean isSync() { return true; }
    void cancel();
}
