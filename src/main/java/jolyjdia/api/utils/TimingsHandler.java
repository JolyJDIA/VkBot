package jolyjdia.api.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TimingsHandler {
    private static final int SAMPLE_INTERVAL = 100;
    public static final double ASPIRATION = 20.0;
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
    @NonNls
    public String memoryUsed() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return "\nВся память: " + humanReadableByteCount(totalMemory) +
               "\nСъедено памяти: " + humanReadableByteCount((totalMemory - freeMemory)) +
               "\nСвободно памяти: " + humanReadableByteCount(freeMemory);
    }
    private static String humanReadableByteCount(long bytes) {
        if (bytes < 1000) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1000));
        String pre = String.valueOf("kMGTPE".charAt(exp - 1));
        return String.format("%.1f %sB", bytes / Math.pow(1000, exp), pre);
    }
}