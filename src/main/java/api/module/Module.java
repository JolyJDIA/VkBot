package api.module;

public interface Module {
    default void onLoad() {}
    default void onReload() {}
}

