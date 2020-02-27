package jolyjdia.api.scheduler;

public class TaskAsync extends Task {

    public TaskAsync(Runnable o, long period, int id) {
        super(o, period, id);
    }

    @Override
    public final boolean isAsync() {
        return true;
    }
}