package api.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.TreeSet;
import java.util.concurrent.Executor;

public class CommandExecutor implements Executor {

    private TreeSet<Worker> workers = new TreeSet<>();

    public CommandExecutor(int workersCount) {
        for (int i = 0; i < workersCount; i++) {
            Worker worker = new Worker();

            worker.start();

            workers.add(worker);
        }
    }

    @Override
    public void execute(@NotNull Runnable command) {
        workers.first().addTask(command);
    }

}
