package api.utils;

import api.Bot;
import org.jetbrains.annotations.Contract;

import java.util.Timer;
import java.util.TimerTask;

public final class Watchdog {
    private static final TimingsHandler timingsHandler = new TimingsHandler();

    @Contract(pure = true)
    private Watchdog() {}

    public static void doStart() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Bot.getScheduler().mainThreadHeartbeat();
                timingsHandler.tick();
            }
        }, 0, 50);
    }

    @Contract(pure = true)
    public static double getAverageTPS() {
        return timingsHandler.getAverageTPS();
    }
}