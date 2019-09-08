package api.utils;

import org.jetbrains.annotations.Contract;

import java.util.Timer;
import java.util.TimerTask;

public final class TickPerSeconds extends TimerTask {
    private static TickPerSeconds ourInstance;
    private final TimingsHandler timingsHandler = new TimingsHandler();
    private TickPerSeconds() {}

    public static void doStart() {
        if (ourInstance == null) {
            ourInstance = new TickPerSeconds();
            new Timer(true).scheduleAtFixedRate(ourInstance, 0, 50);//50
        }
    }

    @Contract(pure = true)
    public static TickPerSeconds getInstance() {
        return ourInstance;
    }

    @Override
    public void run() {
        ObedientBot.SCHEDULER.mainThreadHeartbeat();
        timingsHandler.tick();
    }

    @Contract(pure = true)
    public double getAverageTPS() {
        return timingsHandler.getAverageTPS();
    }
}
