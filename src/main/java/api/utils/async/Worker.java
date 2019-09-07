package api.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Worker extends Thread implements Comparable<Worker> {

    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        try {
            while (true)
                taskQueue.take().run();
        } catch (InterruptedException ignored) {}
    }

    void addTask(Runnable task) {
        taskQueue.add(task);
    }

    @Override
    public int compareTo(@NotNull Worker o) {
        return Integer.compare(taskQueue.size(), o.taskQueue.size());
    }

}
