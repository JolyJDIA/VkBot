package api.scheduler;

import api.utils.ObedientBot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RoflanRunnable implements Runnable {
    private Task task;

    public void cancel() {
        ObedientBot.getScheduler().cancel(task);
    }
    @NotNull
    public Task runTask() {
        return setupTask(ObedientBot.getScheduler().runTask(this));
    }
    @NotNull
    public Task runTaskAsynchronously() {
        return setupTask(ObedientBot.getScheduler().runAsyncTask(this));
    }
    @NotNull
    public Task runTaskLater(int delay) {
        return setupTask(ObedientBot.getScheduler().runTaskLater(this, delay));
    }
    @NotNull
    public Task runTaskLaterAsynchronously(int delay) {
        return setupTask(ObedientBot.getScheduler().runAsyncTaskLater(this, delay));
    }
    @NotNull
    public Task runTaskTimer(int delay, int period) {
        return setupTask(ObedientBot.getScheduler().runTaskTimer(this, delay, period));
    }
    @NotNull
    public Task runTaskTimerAsynchronously(int delay, int period) {
        return setupTask(ObedientBot.getScheduler().runAsyncTaskTimer(this, delay, period));
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
