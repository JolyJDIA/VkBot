package api.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class BotScheduler {
    private final Executor executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().build());
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public final void mainThreadHeartbeat() {
        Task task = taskQueue.peek();
        if(task == null) {
            return;
        }

        System.out.println(task.getCurrentTick() + "  " + task.getPeriod());
        if(task.getCurrentTick() >= task.getPeriod()) {
            if (task.isSync()) {
                task.run();
            } else {
                executor.execute(task);
            }
            if (task.getPeriod() <= Task.NO_REPEATING) {
                System.out.println("УДАЛЯЮ ЗАДАЧУ");
                taskQueue.remove(task);
                return;
            }
            task.setCurrentTickZero();
        }
        task.addCurrentTick();

    }
    @NotNull
    public final Task runTask(Runnable runnable) {
        return sync(runnable, Task.NO_REPEATING, Task.NO_REPEATING);
    }
    @NotNull
    public final Task runTask(Consumer<Task> consumer) {
        return sync(consumer, Task.NO_REPEATING, Task.NO_REPEATING);
    }

    @NotNull
    public final Task scheduleSyncRepeatingTask(Runnable runnable, int delay, int period) {
        return sync(runnable, delay, period);
    }
    @NotNull
    public final Task scheduleSyncRepeatingTask(Consumer<Task> consumer, int delay, int period) {
        return sync(consumer, delay, period);
    }
    @NotNull
    public final Task scheduleSyncDelayTask(Runnable runnable, int delay) {
        return sync(runnable, delay, Task.NO_REPEATING);
    }
    @NotNull
    public final Task scheduleSyncDelayTask(Consumer<Task> consumer, int delay) {
        return sync(consumer, delay, Task.NO_REPEATING);
    }

    @NotNull
    public final Task runTaskAsynchronously(Runnable runnable) {
        return async(runnable, Task.NO_REPEATING, Task.NO_REPEATING);
    }
    @NotNull
    public final Task runTaskAsynchronously(Consumer<Task> consumer) {
        return async(consumer, Task.NO_REPEATING, Task.NO_REPEATING);
    }
    @NotNull
    public final Task scheduleAsyncRepeatingTask(Runnable runnable, int delay, int period) {
        return async(runnable, delay, period);
    }
    @NotNull
    public final Task scheduleAsyncRepeatingTask(Consumer<Task> consumer, int delay, int period) {
        return async(consumer, delay, period);
    }
    @NotNull
    public final Task scheduleAsyncDelayTask(Runnable runnable, int delay) {
        return async(runnable, delay, Task.NO_REPEATING);
    }
    @NotNull
    public final Task scheduleAsyncDelayTask(Consumer<Task> consumer, int delay) {
        return async(consumer, delay, Task.NO_REPEATING);
    }
    public final void cancel(Task task) {
        taskQueue.remove(task);
    }
    public final void cancelTasks() {
        taskQueue.clear();
    }

    @NotNull
    private Task sync(Object o, int delay, int period) {
        Task task = new Task(o, delay, period);
        taskQueue.add(task);
        return task;
    }
    @NotNull
    private Task async(Object o, int delay, int period) {
        TaskAsync task = new TaskAsync(o, delay, period);
        taskQueue.add(task);
        return task;
    }

}
