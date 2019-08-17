package api;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public abstract class JavaModule {
    private static final Set<JavaModule> modules = new HashSet<>();

    protected JavaModule() {
        modules.add(this);
    }
    public abstract void onLoad();

    @Contract(pure = true)
    public static int getModulesSize() {
        return modules.size();
    }

}
