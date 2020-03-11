package jolyjdia.api.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TimingsHandler {
    private static final int SAMPLE_INTERVAL = 100;
    public static final double ASPIRATION = 20.0;
    private final double[] recentTps = {20, 20, 20};
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
    private static double calcTps(double avg, double exp, double tps) {
        return (avg * exp) + (tps * (1 - exp));
    }

    public final double[] getAverageTPS() {
        return recentTps;
    }
    @NonNls
    public static @NotNull String format(double tps) {
        return ((tps > ASPIRATION) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }
    @NotNull
    @NonNls
    public static String memoryUsed() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return "\nTotal memory: " + humanReadableByteCount(totalMemory) +
               "\nUsed memory: " + humanReadableByteCount((totalMemory - freeMemory)) +
               "\nFree memory: " + humanReadableByteCount(freeMemory);
    }
    public static String humanReadableByteCount(long bytes) {
        String s = bytes < 0 ? "-" : "";
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1000L ? bytes + " B"
                : b < 999_950L ? String.format("%s%.1fkB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1fMB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1fGB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1fTB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1fPB", s, b / 1e3)
                : String.format("%s%.1fEB", s, b / 1e6);
    }
}