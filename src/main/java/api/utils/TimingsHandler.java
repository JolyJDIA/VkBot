package api.utils;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class TimingsHandler {
    private long lastPoll = System.nanoTime();
    private final List<Long> tpsRecent = new ArrayList<>();

    public final void tick() {
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
    @Contract(pure = true)
    final double getAverageTPS() {
        int avg = 0;
        for (long f : tpsRecent) {
            avg += f;
        }
        return avg / tpsRecent.size();
    }
}