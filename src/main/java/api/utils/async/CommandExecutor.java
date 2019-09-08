package api.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author ItzFreshyyy
 */

public class CommandExecutor implements Executor {

    private final List<Worker> workers = new ArrayList<>();

    public CommandExecutor(int workersCount) {
        for (int i = 0; i < workersCount; i++) {
            Worker worker = new Worker();

            worker.start();

            workers.add(worker);
        }
    }

    @Override
    public void execute(@NotNull Runnable command) {
        workers.sort(null);

        workers.get(0).addTask(command);
    }

}
