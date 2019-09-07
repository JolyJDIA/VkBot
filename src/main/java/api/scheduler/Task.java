package api.scheduler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Task implements TypeTask, Runnable {
    public static final int NO_REPEATING = -1;
    private int delay = NO_REPEATING;
    private int period = NO_REPEATING;
    private int currentTick;

    private Runnable runnable;
    private Consumer<TypeTask> consumer;

    public Task(@NotNull Object o) {
        if (Runnable.class.isAssignableFrom(o.getClass())) {
            this.runnable = (Runnable) o;
        } else if (Consumer.class.isAssignableFrom(o.getClass())) {
            this.consumer = (Consumer<TypeTask>) o;
        } else {
            throw new AssertionError("Illegal task class");
        }
    }

    public Task(Object o, int delay, int period) {
        this(o);
        this.delay = delay;
        this.period = period;
    }
    @Override
    public final void run() {
        if (this.runnable != null) {
            this.runnable.run();
        } else {
            this.consumer.accept(this);
        }
    }

    @Contract(pure = true)
    public final int getDelay() {
        return delay;
    }

    @Contract(pure = true)
    public final int getPeriod() {
        return period;
    }

    public final void setCurrentTickZero() {
        this.currentTick = 0;
    }
    @Contract(pure = true)
    public final Consumer<TypeTask> getConsumer() {
        return consumer;
    }

    @Contract(pure = true)
    public final int getCurrentTick() {
        return currentTick;
    }
    public final void addCurrentTick() {
        this.currentTick += 1;
    }
}