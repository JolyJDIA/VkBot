package api.scheduler;

import org.jetbrains.annotations.Contract;

public class TaskAsync extends Task {

    public TaskAsync(Object o, int period, int delay) {
        super(o, period, delay);
    }

    @Contract(pure = true)
    @Override
    public final boolean isSync() {
        return false;
    }
}
