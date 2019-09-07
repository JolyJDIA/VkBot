package api.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BotScheduler {
    private final Executor executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().build());
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public final void mainThreadHeartbeat() {
        Task task = taskQueue.peek();
        if(task == null) {
            return;
        }
        if(task.getCurrentTick() >= task.getDelay()
                && task.getCurrentTick() >= task.getPeriod()) {
            if (task.isSync()) {
                task.getRunnable().run();
            } else {
                executor.execute(task.getRunnable());
            }
            task.setCurrentTickZero();
        }
        if(task.getPeriod() > 0) {
            task.addCurrentTick();
        } else {
            taskQueue.remove(task);
        }
    }
    public final void runSyncTask(Runnable runnable) {
        taskQueue.add(new Task(runnable));
    }
    public final void runSyncTaskTimer(Runnable runnable, int period) {
        taskQueue.add(new Task(runnable, 0, period));
    }
    public final void runSyncTaskLater(Runnable runnable, int delay) {
        taskQueue.add(new Task(runnable, delay, 0));
    }
    public final void runAsyncTask(Runnable runnable) {
        taskQueue.add(new TaskAsync(runnable));
    }
    public final void runAsyncTaskTimer(Runnable runnable, int period) {
        taskQueue.add(new TaskAsync(runnable, 0, period));
    }
    public final void runAsyncTaskLater(Runnable runnable, int delay) {
        taskQueue.add(new TaskAsync(runnable, delay, 0));
    }
}
