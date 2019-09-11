package api.utils;

import org.jetbrains.annotations.Contract;

import java.util.Timer;
import java.util.TimerTask;

public final class Watchdog extends TimerTask {
    private static Watchdog ourInstance;
    private static final TimingsHandler timingsHandler = new TimingsHandler();
    private Watchdog() {}

    public static void doStart() {
        if (ourInstance != null) {
            return;
        }
        ourInstance = new Watchdog();
        new Timer(true).scheduleAtFixedRate(ourInstance, 0, 50);//50
    }

    @Contract(pure = true)
    public static Watchdog getInstance() {
        return ourInstance;
    }

    @Override
    public void run() {
        ObedientBot.SCHEDULER.mainThreadHeartbeat();
        timingsHandler.tick();
    }

    @Contract(pure = true)
    public static double getAverageTPS() {
        return timingsHandler.getAverageTPS();
    }
}