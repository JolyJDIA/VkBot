package scheduler;

public class TaskSync extends Task {

    public TaskSync(Runnable o, long period, int id) {
        super(o, period, id);
    }

    @Override
    public final boolean isAsync() {
        return false;
    }
}
