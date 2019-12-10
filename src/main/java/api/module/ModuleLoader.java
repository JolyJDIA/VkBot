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
        modules.forEach(e -> {
            e.onDisable();
         //   e.onLoad();
        });
    }
    public final void disableModule() {
        modules.forEach(Module::onDisable);
    }
    public final void registerModule(Module module) {
        modules.add(module);
    }
}
