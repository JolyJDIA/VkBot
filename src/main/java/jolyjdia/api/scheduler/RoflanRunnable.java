package jolyjdia.api.scheduler;

import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

public abstract class RoflanRunnable implements Runnable {
    private Task task;

    public final void removeTask() {
        Bot.getScheduler().removeTask(task);
    }
    public final void cancel() {
        task.cancel();
    }
    public final Task runTask() {
        return setupTask(Bot.getScheduler().runTask(this));
    }

    public final void runAsyncTask() {
        Bot.getScheduler().runAsyncTask(this);
    }
    public final Task runSyncTaskAfter(int delay) {
        return setupTask(Bot.getScheduler().runSyncTaskAfter(this, delay));
    }
    public final Task runAsyncTaskAfter(int delay) {
        return setupTask(Bot.getScheduler().runAsyncTaskAfter(this, delay));
    }
    public final Task runRepeatingSyncTaskAfter(int delay, int period) {
        return setupTask(Bot.getScheduler().runRepeatingSyncTaskAfter(this, delay, period));
    }
    public final Task runRepeatingAsyncTaskAfter(int delay, int period) {
        return setupTask(Bot.getScheduler().runRepeatingAsyncTaskAfter(this, delay, period));
    }

    private Task setupTask(@NotNull Task task) {
        this.task = task;
        return task;
    }
}
