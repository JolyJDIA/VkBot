package joly.scheduler;


import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Task  {
    protected long offset;
    protected long period;
    @Nullable protected Runnable runnableBody;
    protected long timestamp;
    protected ScheduledTaskState state;
    protected long sequenceNumber;
    protected String name;
    protected TaskSynchroncity syncType;

    // Internal Task state. Not for user-service use.
    public enum ScheduledTaskState {
        WAITING,
        RUNNING,
        CANCELED,
    }

    protected Task(@Nullable Runnable runnable, long x, long t, TaskSynchroncity syncType, long sequenceNumber) {
        this.state = ScheduledTaskState.WAITING;
        this.runnableBody = runnable;
        this.syncType = syncType;
        this.offset = x;
        this.period = t;
        this.sequenceNumber = sequenceNumber;
    }

    // Builder method
    protected final Task setState() {
        this.state = ScheduledTaskState.RUNNING;
        return this;
    }

    // Builder method
    protected Task setOffset(long x) {
        this.offset = x;
        return this;
    }

    // Builder method
    protected Task setPeriod(long t) {
        this.period = t;
        return this;
    }

    // Builder method
    protected Task setTimestamp(long ts) {
        this.timestamp = ts;
        return this;
    }


    public final Optional<Long> getDelay() {
        Optional<Long> result = Optional.empty();
        if (this.offset > 0) {
            result = Optional.of(this.offset);

        }
        return result;
    }

    public Optional<Long> getInterval() {
        Optional<Long> result = Optional.empty();

        if (this.period > 0) {
            result = Optional.of(this.period);
        }
        return result;
    }

    public boolean cancel() {
        boolean bResult = false;
        if (this.state == Task.ScheduledTaskState.WAITING) {
            bResult = true;
        }
        this.state = Task.ScheduledTaskState.CANCELED;

        return bResult;
    }

    public Optional<Runnable> getRunnable() {
        Optional<Runnable> result = Optional.empty();
        if (this.runnableBody != null) {
            result = Optional.of(this.runnableBody);
        }
        return result;
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Optional<String> getName() {
        Optional<String> result = Optional.empty();
        if (this.name != null) {
            result = Optional.of(this.name);
        }
        return result;
    }
    public boolean isSynchronous() {
        return this.syncType == TaskSynchroncity.SYNCHRONOUS;
    }

    public final String setName(String name) {
        this.name = name;
        return name;
    }
    public enum TaskSynchroncity {
        SYNCHRONOUS,
        ASYNCHRONOUS
    }
}