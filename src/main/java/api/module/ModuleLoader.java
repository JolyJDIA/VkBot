package api.module;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class ModuleLoader {
    private final Set<Module> modules = new HashSet<>();

    @Contract(pure = true)
    public final Set<Module> getModules() {
        return modules;
    }
    public final void reloadModule() {
        modules.forEach(Module::onReload);
    }
    public final void registerModule(Module module) {
        modules.add(module);
        module.onLoad();
    }
}
