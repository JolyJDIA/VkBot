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
    private static final AtomicInteger ids = new AtomicInteger();
    private final RoflanBlockingQueue taskQueue = new RoflanBlockingQueue();
    private final ExecutorService executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("BotScheduler Thread %d")
                    .build()
    );
    private int counter;
    private final TimingsHandler handler = new TimingsHandler();

    public BotScheduler() {
        mainThreadHeartbeat();
    }
    public final void mainThreadHeartbeat() {
        new Thread(() -> {
            while (true) {
                System.out.println("dsdasd");
                handler.tick();
                if (taskQueue.isEmpty()) {
                    return;
                }
                Task task = taskQueue.peek();
                if (counter >= task.getNextRun()) {
                    if (task.isAsync()) {
                        executor.execute(task);
                    } else {
                        task.run();
                    }
                    if (task.isCancelled()) {
                        taskQueue.remove();
                        System.out.println((task.isAsync() ? "Async" : "Sync") + "Scheduler: task deleted (" + taskQueue.size() + ')');
                        return;
                    }
                    taskQueue.setNexRun(counter + task.getPeriod());
                }
                ++counter;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public <T> Future<T> submitAsync(Callable<T> callable) {
        return executor.submit(callable);
    }

    @NotNull
    public final Task runSyncTask(Runnable runnable) {
        return sync(runnable, Task.NO_REPEATING, Task.NO_REPEATING);
    }

    public final void runAsyncTask(Runnable runnable) {
        executor.execute(runnable);
    }

    @NotNull
    public final Task runRepeatingSyncTaskAfter(Runnable runnable, int delay, int period) {
        return sync(runnable, delay, period);
    }

    @NotNull
    public final Task runSyncTaskAfter(Runnable runnable, int delay) {
        return sync(runnable, delay, Task.NO_REPEATING);
    }

    @NotNull
    public final Task runAsyncTaskAfter(Runnable runnable, int delay) {
        return async(runnable, delay, Task.NO_REPEATING);
    }

    @NotNull
    public final Task runRepeatingAsyncTaskAfter(Runnable runnable, int delay, int period) {
        return async(runnable, delay, period);
    }

    public final void cancelTask(@NotNull Task task) {
        task.cancel();
    }

    public final void cancelTasks() {
        taskQueue.clear();
    }

    public final void removeTask(@NotNull Task task) {
        taskQueue.remove(task);
    }

    @NotNull
    private Task sync(Runnable command, long delay, long period) {
        if (command == null) {
            throw new NullPointerException();
        }
        TaskSync task = new TaskSync(command, period, ids.getAndIncrement());//incrementGetAnd
        return addTask(task, delay);
    }

    @NotNull
    private Task async(Runnable command, long delay, long period) {
        if (command == null) {
            throw new NullPointerException();
        }
        TaskAsync task = new TaskAsync(command, period, ids.getAndIncrement());
        return addTask(task, delay);
    }

    @NotNull
    @Contract("_, _ -> param1")
    private Task addTask(@NotNull Task task, long delay) {
        task.setNextRun(counter + delay);
        taskQueue.add(task);
        return task;
    }

    public int getCounter() {
        return counter;
    }

    public final TimingsHandler getTimingsHandler() {
        return handler;
    }
}