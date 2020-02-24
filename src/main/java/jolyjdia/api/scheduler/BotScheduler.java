package jolyjdia.api.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jolyjdia.api.utils.TimingsHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BotScheduler {
    private final AtomicInteger ids = new AtomicInteger(1);
    private final RoflanBlockingQueue taskQueue = new RoflanBlockingQueue();
    private final Executor executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("BotScheduler Thread %d")
                    .build()
    );
    private int counter;
    private final TimingsHandler timingsHandler = new TimingsHandler();

    public final void mainThreadHeartbeat() {
        Iterator<Task> iterator = taskQueue.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (counter >= task.getNextRun()) {
                if(task.isAsync()) {
                    executor.execute(task);
                } else {
                    task.run();
                }
                if (task.isCancelled()) {
                    iterator.remove();
                    return;
                }
                task.setNextRun(counter+task.getPeriod());
            }
        }
        ++counter;
    }

    @NotNull
    public final Task runTask(Runnable runnable) {
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

    //тут можно обосраться
    public final void removeTask(Task task) {
        taskQueue.remove(task);
        task.cancel();
    }

    public final void cancelTasks() {
        taskQueue.clear();
    }
    public final TimingsHandler getTimingsHandler() {
        return timingsHandler;
    }

    @NotNull
    private Task sync(Runnable runnable, long delay, long period) {
        TaskSync task = new TaskSync(runnable, period, nextId());
        task.setNextRun(counter + delay);
        taskQueue.add(task);
        return task;
    }

    @NotNull
    private Task async(Runnable runnable, long delay, long period) {
        TaskAsync task = new TaskAsync(runnable, period, nextId());
        task.setNextRun(counter + delay);
        taskQueue.add(task);
        return task;
    }

    public final void addTask(Task task) {
        taskQueue.add(task);
    }

    public int taskCount() {
        return taskQueue.size();
    }

    public RoflanBlockingQueue getTaskQueue() {
        return taskQueue;
    }

    private int nextId() {
        return ids.incrementAndGet();
    }

    public int getCounter() {
        return counter;
    }
}
