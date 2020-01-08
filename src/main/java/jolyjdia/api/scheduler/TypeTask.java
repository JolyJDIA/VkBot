package jolyjdia.api.scheduler;

public interface TypeTask {
    default boolean isAsync() { return false; }
}
