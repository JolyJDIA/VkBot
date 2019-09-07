package api.scheduler;

public interface TypeTask {
    default boolean isSync() {
        return true;
    }
}
