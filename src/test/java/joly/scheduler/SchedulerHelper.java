package joly.scheduler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class SchedulerHelper {
    private long sequenceNumber;
    private Task.TaskSynchroncity syncType;

    private SchedulerHelper() {}

    protected SchedulerHelper(Task.TaskSynchroncity syncType) {
        this.syncType = syncType;
    }

    protected static Optional<Task> getTaskById(@NotNull Map<Long, ? extends Task> taskMap, long id) {
        Optional<Task> resultTask = Optional.empty();

        Task tmp = taskMap.get(id);
        if (tmp != null) {
            resultTask = Optional.of(tmp);
        }

        return resultTask;
    }

    @NotNull
    @Contract("_ -> new")
    protected static Collection<Task> getScheduledTasks(Map<Long, ? extends Task> taskMap) {
        return new ArrayList<>(taskMap.values());
    }


    protected static Optional<Long> getUuidOfTaskByName(@NotNull Map<Long, ? extends Task> taskMap, String name) {
        Optional<Long> id = Optional.empty();

        for (Task t : taskMap.values()) {
            if (name.equals(t.name)) {
                return Optional.of(t.sequenceNumber);
            }
        }
        return id;

    }

    protected final Task taskValidationStep(Runnable runnableTarget, long offset, long period) {
        this.sequenceNumber++;
        Task tmpTask = new Task(runnableTarget, offset, period, this.syncType, sequenceNumber);
        tmpTask.setName("Task"+this.sequenceNumber);
        return tmpTask;
    }

}