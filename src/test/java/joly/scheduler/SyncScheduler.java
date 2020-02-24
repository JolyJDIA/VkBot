package joly.scheduler;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SyncScheduler {
    private final Map<Long, Task> taskMap = new ConcurrentHashMap<>();
    // The internal counter of the number of Ticks elapsed since this Scheduler was listening for
    // ServerTickEvent from Forge.
    private volatile long counter;
    private static final long sequenceNumber = 0L;

    // Query actor for task information
    private final SchedulerHelper schedulerHelper;

    public SyncScheduler() {
        this.schedulerHelper = new SchedulerHelper(Task.TaskSynchroncity.SYNCHRONOUS);
    }
    public Optional<Task> getTaskById(long id) {
        return SchedulerHelper.getTaskById(this.taskMap, id);
    }
    public Optional<Long> getUuidOfTaskByName(String name) {

        return SchedulerHelper.getUuidOfTaskByName(this.taskMap, name);
    }

    public Collection<Task> getScheduledTasks() {
        return SchedulerHelper.getScheduledTasks(this.taskMap);
    }

    public void processTasks() {
        ++counter;
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
            long now = this.counter;
            if (threshold <= (now - task.timestamp)) {
                task.timestamp = this.counter;
                if(task.runnableBody == null) {
                    return;
                }
                task.runnableBody.run();//sync execute
                task.setState();
                if (task.period == 0L) {
                    this.taskMap.remove(task.getSequenceNumber());
                }

            }
        }
    }
    public final Task runTask(Runnable runnableTarget) {
        Task nonRepeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, 0, 0);
        taskMap.put(nonRepeatingTask.getSequenceNumber(), nonRepeatingTask);
        return nonRepeatingTask;
    }
    public Task runTaskAfter(Runnable runnableTarget, long delay) {
        Task nonRepeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, delay, 0);
        taskMap.put(nonRepeatingTask.getSequenceNumber(), nonRepeatingTask);
        return nonRepeatingTask;
    }

    public Task runRepeatingTask(Runnable runnableTarget, long interval) {
        Task repeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, 0, interval);
        taskMap.put(repeatingTask.getSequenceNumber(), repeatingTask);
        return repeatingTask;
    }
    public Task runRepeatingTaskAfter(Runnable runnableTarget, long interval, long delay) {
        Task repeatingTask = this.schedulerHelper.taskValidationStep(runnableTarget, delay, interval);
        taskMap.put(repeatingTask.getSequenceNumber(), repeatingTask);
        return repeatingTask;
    }
}