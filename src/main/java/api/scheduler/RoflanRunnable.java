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
        return setupTask(ObedientBot.SCHEDULER.runTaskAsynchronously(this));
    }
    @NotNull
    public Task runTaskLater(int delay) {
        return setupTask(ObedientBot.SCHEDULER.scheduleSyncDelayTask(this, delay));
    }
    @NotNull
    public Task runTaskLaterAsynchronously(int delay) {
        return setupTask(ObedientBot.SCHEDULER.scheduleAsyncDelayTask(this, delay));
    }
    @NotNull
    public Task runTaskTimer(int delay, int period) {
        return setupTask(ObedientBot.SCHEDULER.scheduleSyncRepeatingTask(this, delay, period));
    }
    @NotNull
    public Task runTaskTimerAsynchronously(int delay, int period) {
        return setupTask(ObedientBot.SCHEDULER.scheduleAsyncRepeatingTask(this, delay, period));
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
