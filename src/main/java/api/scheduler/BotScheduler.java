package api.scheduler;

import api.utils.TimingsHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class BotScheduler {
    private final Executor executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
            .setNameFormat("BotScheduler Thread %d")
            .build()
    );

    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private final TimingsHandler timingsHandler = new TimingsHandler();

    public final void mainThreadHeartbeat() {
        timingsHandler.tick();
        Iterator<Task> iterator = taskQueue.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getCurrentTick() >= task.getPeriod()) {
                if (task.isAsync()) {
                    executor.execute(task);
                } else {
                    task.run();
                }
                if (task.getPeriod() <= Task.NO_REPEATING) {
                    iterator.remove();
                    return;
                }
                task.setCurrentTickZero();
            }
            task.addCurrentTick();
        }
    }
    public final TimingsHandler getTimingsHandler() {
        return timingsHandler;
    }

    public final @NotNull Task runTask(Runnable runnable) {
        return sync(runnable, Task.NO_REPEATING, Task.NO_REPEATING);
    }

    public final @NotNull Task runTask(Consumer<Task> consumer) {
        return sync(consumer, Task.NO_REPEATING, Task.NO_REPEATING);
    }

    public final @NotNull Task scheduleSyncRepeatingTask(Runnable runnable, int delay, int period) {
        return sync(runnable, delay, period);
    }

    public final @NotNull Task scheduleSyncRepeatingTask(Consumer<Task> consumer, int delay, int period) {
        return sync(consumer, delay, period);
    }

    public final @NotNull Task scheduleSyncDelayTask(Runnable runnable, int delay) {
        return sync(runnable, delay, Task.NO_REPEATING);
    }

    public final @NotNull Task scheduleSyncDelayTask(Consumer<Task> consumer, int delay) {
        return sync(consumer, delay, Task.NO_REPEATING);
    }

    public final @NotNull Task runTaskAsynchronously(Runnable runnable) {
        return async(runnable, Task.NO_REPEATING, Task.NO_REPEATING);
    }
    public final @NotNull Task runTaskAsynchronously(Consumer<Task> consumer) {
        return async(consumer, Task.NO_REPEATING, Task.NO_REPEATING);
    }
    public final @NotNull Task scheduleAsyncRepeatingTask(Runnable runnable, int delay, int period) {
        return async(runnable, delay, period);
    }
    public final @NotNull Task scheduleAsyncRepeatingTask(Consumer<Task> consumer, int delay, int period) {
        return async(consumer, delay, period);
    }
    public final @NotNull Task scheduleAsyncDelayTask(Runnable runnable, int delay) {
        return async(runnable, delay, Task.NO_REPEATING);
    }
    public final @NotNull Task scheduleAsyncDelayTask(Consumer<Task> consumer, int delay) {
        return async(consumer, delay, Task.NO_REPEATING);
    }

    public final void cancel(Task task) {
        taskQueue.remove(task);
    }

    public final void cancelTasks() {
        taskQueue.clear();
    }

    private @NotNull Task sync(Object o, int delay, int period) {
        Task task = new Task(o, delay, period);
        taskQueue.add(task);
        return task;
    }

    private @NotNull Task async(Object o, int delay, int period) {
        TaskAsync task = new TaskAsync(o, delay, period);
        taskQueue.add(task);
        return task;
    }

    public int taskCount() {
        return taskQueue.size();
    }
}