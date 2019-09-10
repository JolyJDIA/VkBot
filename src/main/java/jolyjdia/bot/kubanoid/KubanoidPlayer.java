package jolyjdia.bot.kubanoid;

import org.jetbrains.annotations.Contract;

public class KubanoidPlayer {
    private boolean win;
    private int score;

    @Contract(pure = true)
    public final boolean isWin() {
        return win;
    }

    @Contract(pure = true)
    public final int getScore() {
        return score;
    }

    public final void setWin(boolean win) {
        this.win = win;
    }

    public final int addScore(int score) {
        this.score += score;
        return this.score;
    }
}
