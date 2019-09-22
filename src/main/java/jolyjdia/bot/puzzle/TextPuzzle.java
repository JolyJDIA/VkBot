package jolyjdia.bot.puzzle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TextPuzzle implements Answer {
    private static final String[] WORDS = {
            "Лестница",
            "Дорога",
            "Владивосток",
            "Переселенец",
            "Одно",
            "Гвозди",
            "Слоненок",
            "Холодильник",
            "Бразилиа"
    };
    private static final String[] ASKS = {
            "По чему ходят часто, а ездят редко?",
            "Идет то в гору, то с горы, но остается на месте.",
            "В каком городе спрятались мужское имя и сторона света?",
            "В каком слове 5 \"е\" и никаких других гласных?",
            "Сколько лет в году?",
            "Они бывают металлические и жидкие. О чем речь?",
            "Маленький, серенький, похож на слона. Кто это?",
            "К тебе пришли гости, а в холодильнике — бутылка лимонада, пакет с ананасовым соком и бутылка минеральной воды. Что ты откроешь в первую очередь?",
            "Столица Бразилии?"
    };
    private static int index = -1;
    TextPuzzle() {
        index = (index == WORDS.length-1) ? 0 : ++index;
    }
    @Contract(pure = true)
    @Override
    public final String getAnswer() {
        return WORDS[index];
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public final String getStringFormatAnswer() {
        return "Решите загадку: "+ASKS[index];
    }
}
