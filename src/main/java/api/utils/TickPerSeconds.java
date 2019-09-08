package api.utils;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class TickPerSeconds extends TimerTask {
    private static TickPerSeconds ourInstance;
    private long lastPoll = System.nanoTime();
    private static final List<Long> tpsRecent = new ArrayList<>();
    private TickPerSeconds() {}

    public static void doStart() {
        if (ourInstance == null) {
            ourInstance = new TickPerSeconds();
            new Timer(true).scheduleAtFixedRate(ourInstance, 0, 50);//50
        }
    }

    @Contract(pure = true)
    public static double getAverageTPS() {
        int avg = 0;
        for (long f : tpsRecent) {
            avg += f;
        }
        return avg / tpsRecent.size();
    }
    private short tick;

    @Override
    public void run() {
        ObedientBot.SCHEDULER.mainThreadHeartbeat();
        long startTime = System.nanoTime();
        long timeSpent = (startTime - lastPoll) / 1000;
        if (timeSpent == 0) {
            timeSpent = 1;
        }
        long tps = 50000000L / timeSpent;
        if(tpsRecent.size() > 9) {
            tpsRecent.clear();
        }
        tpsRecent.add(tps);
        lastPoll = startTime;
    }
}
