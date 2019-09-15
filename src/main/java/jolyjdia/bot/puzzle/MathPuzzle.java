package jolyjdia.bot.puzzle;

import api.utils.MathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MathPuzzle implements Answer {
    private final int answer;
    @NonNls private final String format;
    MathPuzzle() {
        int token = MathUtils.RANDOM.nextInt(2);
        int first = MathUtils.RANDOM.nextInt(1000);
        int second = MathUtils.RANDOM.nextInt(1000);
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
