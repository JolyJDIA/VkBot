package api;

import org.jetbrains.annotations.Contract;

import java.util.Timer;
import java.util.TimerTask;

public final class TickPerSeconds extends TimerTask {
    private static TickPerSeconds ourInstance;
    private long lastPoll = System.nanoTime();
    private static final long[] tpsRecent = new long[10];
    private TickPerSeconds() {}

    public static void doStart() {
        if (ourInstance == null) {
            ourInstance = new TickPerSeconds();
            new Timer(true).scheduleAtFixedRate(ourInstance, 0, 50);
        }
    }

    @Contract(pure = true)
    public static double getAverageTPS() {
        int avg = 0;
        for (long f : tpsRecent) {
            avg += f;
        }
        return avg / tpsRecent.length;
    }
    private int current;

    @Override
    public void run() {
        long startTime = System.nanoTime();
        long timeSpent = (startTime - lastPoll) / 1000;
        if (timeSpent == 0) {
            timeSpent = 1;
        }
        long tps = 50000000L / timeSpent;
        if(current > 9) {
            current = 0;
        }
        tpsRecent[current] = tps;
        ++current;
        lastPoll = startTime;
    }
}
