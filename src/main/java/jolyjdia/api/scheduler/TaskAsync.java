package jolyjdia.api.scheduler;

public class TaskAsync extends Task {

    public TaskAsync(Object o, int period, int delay) {
        super(o, period, delay);
    }

    @Override
    public final boolean isAsync() {
        return true;
    }
}
