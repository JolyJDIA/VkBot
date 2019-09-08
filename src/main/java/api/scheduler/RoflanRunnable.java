package api.scheduler;

import api.utils.ObedientBot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RoflanRunnable implements Runnable {
    private Task task;

    public void cancel() {
        ObedientBot.SCHEDULER.cancel(task);
    }
    @NotNull
    public Task runTask() {
        return setupTask(ObedientBot.SCHEDULER.runTask(this));
    }
    @NotNull
    public Task runTaskAsynchronously() {
        return setupTask(ObedientBot.SCHEDULER.runAsyncTask(this));
    }
    @NotNull
    public Task runTaskLater(int delay) {
        return setupTask(ObedientBot.SCHEDULER.runTaskLater(this, delay));
    }
    @NotNull
    public Task runTaskLaterAsynchronously(int delay) {
        return setupTask(ObedientBot.SCHEDULER.runAsyncTaskLater(this, delay));
    }
    @NotNull
    public Task runTaskTimer(int delay, int period) {
        return setupTask(ObedientBot.SCHEDULER.runTaskTimer(this, delay, period));
    }
    @NotNull
    public Task runTaskTimerAsynchronously(int delay, int period) {
        return setupTask(ObedientBot.SCHEDULER.runAsyncTaskTimer(this, delay, period));
    }
    @Contract("_ -> param1")
    @NotNull
    private Task setupTask(@NotNull Task task) {
        this.task = task;
        return task;
    }

    @Override
    public void run() {}
}
