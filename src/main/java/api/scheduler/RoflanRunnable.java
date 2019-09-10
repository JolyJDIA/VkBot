package api.scheduler;

import api.utils.ObedientBot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RoflanRunnable implements Runnable {
    private Task task;

    public final void cancel() {
        ObedientBot.SCHEDULER.cancel(task);
    }
    public final Task runTask() {
        return setupTask(ObedientBot.SCHEDULER.runTask(this));
    }
    public final Task runTaskAsynchronously() {
        return setupTask(ObedientBot.SCHEDULER.runTaskAsynchronously(this));
    }
    public final Task runTaskLater(int delay) {
        return setupTask(ObedientBot.SCHEDULER.scheduleSyncDelayTask(this, delay));
    }
    public final Task runTaskLaterAsynchronously(int delay) {
        return setupTask(ObedientBot.SCHEDULER.scheduleAsyncDelayTask(this, delay));
    }
    public final Task runTaskTimer(int delay, int period) {
        return setupTask(ObedientBot.SCHEDULER.scheduleSyncRepeatingTask(this, delay, period));
    }
    public final Task runTaskTimerAsynchronously(int delay, int period) {
        return setupTask(ObedientBot.SCHEDULER.scheduleAsyncRepeatingTask(this, delay, period));
    }
    @Contract("_ -> param1")
    private Task setupTask(@NotNull Task task) {
        this.task = task;
        return task;
    }

    @Override
    public void run() {}
}
