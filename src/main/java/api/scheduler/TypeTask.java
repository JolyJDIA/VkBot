package api.scheduler;

@FunctionalInterface
public interface TypeTask {
    default boolean isSync() {
        return true;
    }
    void cancel();
}
