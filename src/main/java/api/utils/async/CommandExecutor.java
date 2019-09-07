package api.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executor;

public class CommandExecutor implements Executor {

    private final SortedSet<Worker> workers = new TreeSet<>();

    public CommandExecutor(int workersCount) {
        for (int i = 0; i < workersCount; i++) {
            Worker worker = new Worker();

            worker.start();

            workers.add(worker);
        }
    }

    @Override
    public final void execute(@NotNull Runnable command) {
        workers.first().addTask(command);
    }
}
