package jolyjdia.api.module;

import com.google.common.collect.Sets;

import java.util.Set;

public class ModuleLoader {
    private final Set<Module> modules = Sets.newHashSet();

    public final Set<Module> getModules() {
        return modules;
    }
    public final void reloadModule() {
        modules.forEach(e -> {
            e.onDisable();
            e.onLoad();
        });
    }
    public void enableModule() {
        modules.forEach(Module::onLoad);
    }
    public final void disableModule() {
        modules.forEach(Module::onDisable);
    }
    public final void registerModule(Module module) {
        modules.add(module);
    }
}
