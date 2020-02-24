package joly.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class AsyncScheduler {
    private final Map<Long, Task> taskMap = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = this.lock.newCondition();
    private final ExecutorService executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("VKScheduler Thread - %d")
                    .build()
    );
    private final SchedulerHelper schedulerHelper;

    public AsyncScheduler() {
        this.schedulerHelper = new SchedulerHelper(Task.TaskSynchroncity.ASYNCHRONOUS);
    }

    public void processTasks() {
        this.lock.lock();
        try {
            try {
                long minimumTimeout = Long.MAX_VALUE;
                this.condition.await(minimumTimeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Task task : this.taskMap.values()) {
                if (task.state == Task.ScheduledTaskState.CANCELED) {
                    this.taskMap.remove(task.getSequenceNumber());
                    continue;
                }

                long threshold = Long.MAX_VALUE;
                if (task.state == Task.ScheduledTaskState.WAITING) {
                    threshold = task.offset;
                } else if (task.state == Task.ScheduledTaskState.RUNNING) {
                    threshold = task.period;
                }
                long now = System.currentTimeMillis();

                if (threshold <= (now - task.timestamp)) {
                    task.timestamp = System.currentTimeMillis();
                    if(task.runnableBody == null) {
                        return;
                    }
                    this.executor.execute(task.runnableBody);
                    task.setState();
                    if (task.period == 0L) {
                        this.taskMap.remove(task.getSequenceNumber());
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    private Task utilityForAddingAsyncTask(Task task) {
        task.setTimestamp(System.currentTimeMillis());
        this.lock.lock();
        try {
            this.taskMap.put(task.getSequenceNumber(), task);
            this.condition.signalAll();
            return task;
        } finally {
            this.lock.unlock();
        }
    }

    public Task runTask(Runnable runnableTarget) {
        Task nonRepeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, 0, 0);
        return utilityForAddingAsyncTask(nonRepeatingTask);
    }

    public Task runTaskAfter(Runnable runnableTarget, TimeUnit scale, long delay) {
        Task nonRepeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, scale.toMillis(delay), 0);
        return utilityForAddingAsyncTask(nonRepeatingTask);
    }

    public Task runRepeatingTask(Runnable runnableTarget, TimeUnit scale, long interval) {
        interval = scale.toMillis(interval);
        Task repeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, 0, interval);
        return utilityForAddingAsyncTask(repeatingTask);
    }

    public Task runRepeatingTaskAfter(Runnable runnableTarget, TimeUnit scale, long interval, long delay) {
        Task repeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, scale.toMillis(delay), scale.toMillis(interval));
        return utilityForAddingAsyncTask(repeatingTask);
    }
    public Optional<Task> getTaskById(long id) {
        Optional<Task> resultTask = Optional.empty();

        Task tmpTask = this.taskMap.get(id);

        if (tmpTask != null) {
            resultTask = Optional.of(tmpTask);
        }
        return resultTask;
    }
}