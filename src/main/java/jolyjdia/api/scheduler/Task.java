package jolyjdia.api.scheduler;

import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public abstract class Task implements Runnable, Comparable<Task> {
    static final int NO_REPEATING = 0;
    private long period = NO_REPEATING;
    private long nextRun;
    private final Runnable runnable;
    private final int id;

    private Task(Runnable runnable, int id) {
        this.runnable = runnable;
        this.id = id;
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

    public Runnable getRunnable() {
        return runnable;
    }

    public final long getPeriod() {
        return period;
    }

    public void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    public long getNextRun() {
        return nextRun;
    }

    public final void cancel() {
        this.period = NO_REPEATING;
    }

    public final boolean isCancelled() {
        return period <= NO_REPEATING;
    }

    public final int getId() {
        return id;
    }

    public long getDelay() {
        return nextRun - Bot.getScheduler().getCounter();
    }

    @Override
    public final boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && id == ((Task) o).id;
    }

    @Override
    public final int hashCode() {
        return id;
    }

    @Override
    public final int compareTo(@NotNull Task o) {
        if (o == this) {
            return 0;
        }
        return (nextRun < o.nextRun) ?
                -1 : ((nextRun == o.nextRun) ?
                (id < o.id ? -1 : 1) : 1);
    }
}
