package api.scheduler;

import api.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RoflanRunnable implements Runnable {
    private Task task;

    public final void cancel() {
        Bot.getScheduler().cancel(task);
    }
    public final Task runTask() {
        return setupTask(Bot.getScheduler().runTask(this));
    }

    @Deprecated
    public final Task runTaskAsynchronously() {
        return setupTask(Bot.getScheduler().runTaskAsynchronously(this));
    }
    public final Task runTaskLater(int delay) {
        return setupTask(Bot.getScheduler().scheduleSyncDelayTask(this, delay));
    }
    @Deprecated
    public final Task runTaskLaterAsynchronously(int delay) {
        return setupTask(Bot.getScheduler().scheduleAsyncDelayTask(this, delay));
    }
    public final Task runTaskTimer(int delay, int period) {
        return setupTask(Bot.getScheduler().scheduleSyncRepeatingTask(this, delay, period));
    }
    @Deprecated
    public final Task runTaskTimerAsynchronously(int delay, int period) {
        return setupTask(Bot.getScheduler().scheduleAsyncRepeatingTask(this, delay, period));
    }
    @Contract("_ -> param1")
    private Task setupTask(@NotNull Task task) {
        this.task = task;
        return task;
    }

    @Override
    public void run() {}
}
