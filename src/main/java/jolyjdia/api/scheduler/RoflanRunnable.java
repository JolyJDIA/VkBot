package jolyjdia.api.scheduler;

import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public abstract class RoflanRunnable implements Runnable {
    private Task task;

    public final void cancel() {
        Bot.getScheduler().cancel(task);
    }
    public final Task runTask() {
        return setupTask(Bot.getScheduler().runTask(this));
    }

    public final Task runTaskAsynchronously() {
        return setupTask(Bot.getScheduler().runTaskAsynchronously(this));
    }
    public final Task runTaskLater(int delay) {
        return setupTask(Bot.getScheduler().scheduleSyncDelayTask(this, delay));
    }
    public final Task runTaskLaterAsynchronously(int delay) {
        return setupTask(Bot.getScheduler().scheduleAsyncDelayTask(this, delay));
    }
    public final Task runTaskTimer(int delay, int period) {
        return setupTask(Bot.getScheduler().scheduleSyncRepeatingTask(this, delay, period));
    }
    public final Task runTaskTimerAsynchronously(int delay, int period) {
        return setupTask(Bot.getScheduler().scheduleAsyncRepeatingTask(this, delay, period));
    }

    private Task setupTask(@NotNull Task task) {
        this.task = task;
        return task;
    }
}
