package api.utils;

import org.jetbrains.annotations.Contract;

public class TimingsHandler {
    private static final int SAMPLE_INTERVAL = 100;
    private final double[] recentTps = {20,20,20};
    private long tickSection = System.currentTimeMillis();
    private long tickCount = 1;

    public final void tick() {
        long curTime = System.currentTimeMillis();
        if (tickCount == SAMPLE_INTERVAL) {
            double currentTps = 1E3 / (curTime - tickSection) * SAMPLE_INTERVAL;
            recentTps[0] = calcTps(recentTps[0], 0.92, currentTps); // 1/exp(5sec/1min)
            recentTps[1] = calcTps(recentTps[1], 0.9835, currentTps); // 1/exp(5sec/5min)
            recentTps[2] = calcTps(recentTps[2], 0.9945, currentTps); // 1/exp(5sec/15min)
            tickSection = curTime;
            tickCount = 1;
        }
        ++tickCount;
    }
    @Contract(pure = true)
    private static double calcTps(double avg, double exp, double tps) {
        return (avg * exp) + (tps * (1 - exp));
    }
    @Contract(pure = true)
    public final double[] getAverageTPS() {
        return recentTps;
    }
}