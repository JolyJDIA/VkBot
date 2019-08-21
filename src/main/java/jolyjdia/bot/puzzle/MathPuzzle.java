package jolyjdia.bot.puzzle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MathPuzzle implements Answer {
    private final int answer;
    @NonNls private final String format;
    MathPuzzle() {
        int token = Puzzle.RANDOM.nextInt(2);
        int first = Puzzle.RANDOM.nextInt(1000);
        int second = Puzzle.RANDOM.nextInt(1000);
        this.answer = token == 0 ? first + second : first - second;
        this.format = first + (token == 0 ? " + " : " - ") + second;
    }

    @NotNull
    @Contract(pure = true)
    public final String getAnswer() {
        return String.valueOf(answer);
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public final String getStringFormatAnswer() {
        return "Решите пример: "+format;
    }
}
