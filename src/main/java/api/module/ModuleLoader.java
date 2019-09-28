package api.module;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public class ModuleLoader {
    private final Set<Module> modules = Sets.newHashSet();

    @Contract(pure = true)
    public final Set<Module> getModules() {
        return modules;
    }
    public final void reloadModule() {
        modules.forEach(Module::onReload);
    }
    public final void registerModule(Module module) {
        modules.add(module);
    }
}
