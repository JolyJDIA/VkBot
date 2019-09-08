package jolyjdia.bot.game;

public class Player {
    private boolean win;
    private int score;

    public boolean isWin() {
        return win;
    }

    public int getScore() {
        return score;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int addScore(int score) {
        this.score += score;
        return this.score;
    }
}
