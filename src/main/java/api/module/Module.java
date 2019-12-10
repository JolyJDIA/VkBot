package api.module;

@FunctionalInterface
public interface Module {
    void onLoad();
    default void onDisable() {
        System.out.println(getClass().getName() + " is not disable");
    }
}

