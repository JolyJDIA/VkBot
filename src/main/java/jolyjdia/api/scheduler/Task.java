package jolyjdia.api.scheduler;

import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public abstract class Task implements Runnable, Comparable<Task> {
    static final int NO_REPEATING = 0;
    private long period = NO_REPEATING;
    private long nextRun;
    private final Runnable runnable;
    private final int uid;

    private Task(Runnable runnable, int id) {
        this.runnable = runnable;
        this.uid = id;
    }

    public Task(Runnable runnable, long period, int id) {
        this(runnable, id);
        this.period = period;
    }

    @Override
    public final void run() {
        if (this.runnable != null) {
            this.runnable.run();
        }
    }
    public abstract boolean isAsync();

    public final Runnable getRunnable() {
        return runnable;
    }

    public final long getPeriod() {
        return period;
    }

    public final void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    public final long getNextRun() {
        return nextRun;
    }

    public final void cancel() {
        this.period = NO_REPEATING;
    }

    public final boolean isCancelled() {
        return period <= NO_REPEATING;
    }

    public final int getUid() {
        return uid;
    }

    public final long getDelay() {
        return nextRun - Bot.getScheduler().getCounter();
    }

    @Override
    public final int hashCode() {
        return uid;
    }
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        //не юзал компаратор, ибо он вернет 0, когда o == this
        return nextRun == task.nextRun && uid == task.uid;
    }
    @Override
    public final int compareTo(@NotNull Task o) {
        if (o == this) {
            return 0;
        }
        //не может быть у объедков одинаковый айди, поэтому без 0
        return (nextRun < o.nextRun) ? -1 : ((nextRun == o.nextRun) ? (uid < o.uid ? -1 : 1) : 1);
    }
}
