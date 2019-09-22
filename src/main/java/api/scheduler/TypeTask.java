package api.scheduler;

@FunctionalInterface
public interface TypeTask {
    //invert
    default boolean isAsync() { return false; }
    void cancel();
}
