package jolyjdia.api.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jolyjdia.api.utils.TimingsHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class BotScheduler {
    private final AtomicInteger ids = new AtomicInteger(1);
    private final RoflanBlockingQueue taskQueue = new RoflanBlockingQueue();
    private final ExecutorService executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("VkScheduler Thread %d")
                    .build()
    );
    private int counter;
    private final TimingsHandler handler = new TimingsHandler();

    public final void mainLoop() {
        handler.tick();
        if(taskQueue.isEmpty()) {
            return;
        }
        Task task = taskQueue.peek();
        long now = System.currentTimeMillis();
        if (now >= task.getNextRun()) {
            if (task.isAsync()) {
                executor.execute(task);
            } else {
                task.run();
            }
            if(task.isCancelled()) {
                taskQueue.remove();
                return;
            }
            taskQueue.setNexRun(now+task.getPeriod());
        }
        ++counter;
    }

    public <T> Future<T> submitAsync(Callable<T> callable) {
        return executor.submit(callable);
    }

    public Task runSyncTask(Runnable runnable) {
        return this.sync(runnable, Task.NO_REPEATING, Task.NO_REPEATING);
    }

    public final void runAsyncTask(Runnable runnable) {
        executor.execute(runnable);
    }

    @NotNull
    public final Task runRepeatingSyncTaskAfter(Runnable runnable, int delay, int period) {
        return this.sync(runnable, delay, period);
    }

    @NotNull
    public final Task runSyncTaskAfter(Runnable runnable, int delay) {
        return this.sync(runnable, delay, Task.NO_REPEATING);
    }

    @NotNull
    public final Task runAsyncTaskAfter(Runnable runnable, int delay) {
        return this.async(runnable, delay, Task.NO_REPEATING);
    }

    @NotNull
    public final Task runRepeatingAsyncTaskAfter(Runnable runnable, int delay, int period) {
        return this.async(runnable, delay, period);
    }

    public final void cancelTask(@NotNull Task task) {
        task.cancel();
    }

    public final void cancelTasks() {
        taskQueue.clear();
    }

    @NotNull
    private Task sync(Runnable runnable, long delay, long period) {
        TaskSync task = new TaskSync(runnable, period, nextId());
        return addTask(task, delay);
    }

    @NotNull
    private Task async(Runnable runnable, long delay, long period) {
        TaskAsync task = new TaskAsync(runnable, period, nextId());
        return addTask(task, delay);
    }

    @NotNull
    @Contract("_, _ -> param1")
    private Task addTask(@NotNull Task task, long delay) {
        task.setNextRun(counter + delay);
        taskQueue.add(task);
        return task;
    }

    private int nextId() {
        return ids.incrementAndGet();
    }

    public int getCounter() {
        return counter;
    }

    public TimingsHandler getTimingsHandler() {
        return handler;
    }
}