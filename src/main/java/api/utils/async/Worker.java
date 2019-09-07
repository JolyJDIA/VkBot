package api.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Worker extends Thread implements Comparable<Worker> {

    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    @Override
    public final void run() {
        try {
            while (!isInterrupted()) {
                taskQueue.take().run();
            }
        } catch (InterruptedException ignored) {}
    }

    final void addTask(Runnable task) {
        taskQueue.add(task);
    }

    @Override
    public final int compareTo(@NotNull Worker o) {
        return Integer.compare(taskQueue.size(), o.taskQueue.size());
    }
}