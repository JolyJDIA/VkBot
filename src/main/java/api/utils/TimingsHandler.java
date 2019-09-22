package api.utils;

import org.jetbrains.annotations.Contract;

public class TimingsHandler {
    private long currentSec;
    private int ticks;
    private int tps;

    public final void tick() {
        long sec = System.currentTimeMillis() / 1000;
        if(currentSec == sec) {
            ticks++;
        } else {
            currentSec = sec;
            tps = (tps + ticks) / 2;
            ticks = 0;
        }
    }
    @Contract(pure = true)
    public final int getAverageTPS() {
        return tps;
    }
}