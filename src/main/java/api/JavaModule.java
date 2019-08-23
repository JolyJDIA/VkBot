package api;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class JavaModule {
    private static final Set<JavaModule> modules = new HashSet<>();

    protected JavaModule() {
        modules.add(this);
    }

    public void onLoad() {}
    public void onReload() {}

    @Contract(pure = true)
    public static int getModulesSize() {
        return modules.size();
    }
    public static void reloadModule() {
        modules.forEach(JavaModule::onReload);
    }
}
