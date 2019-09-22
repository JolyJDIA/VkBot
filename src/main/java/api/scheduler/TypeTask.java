package api.scheduler;

@FunctionalInterface
public interface TypeTask {
    default boolean isAsync() { return false; }
    void cancel();
}
