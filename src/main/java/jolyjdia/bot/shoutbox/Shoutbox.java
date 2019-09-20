package jolyjdia.bot.shoutbox;

import jolyjdia.bot.Loader;
import jolyjdia.bot.shoutbox.similarity.Cosine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * ГОВНО
 */

final class Shoutbox {
    private static final Pattern TIME = Pattern.compile("%time%");
    private static final Pattern DATE = Pattern.compile("%date%");
    private static final Pattern RANDOM = Pattern.compile("%random%");
    private static final Pattern OR = Pattern.compile(" ~ ");
    private static final Map<String, String> DIAGLOG = new HashMap<>();
    static final Cosine COSINE = new Cosine(2);
    private static final Pattern SPLIT = Pattern.compile(" : ");

    static {
        try (Stream<String> stream = Files.lines(Paths.get(
                Loader.class.getClassLoader().getResource("base.txt").getFile()
        ), StandardCharsets.UTF_8))  {
            stream.forEach(line -> {
                String[] txt = SPLIT.split(line.trim());
                DIAGLOG.put(txt[0], txt[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Contract(pure = true)
    private Shoutbox() {}
    /**
     * ГОВНО
     */
    private static String replacementKey(@NotNull String text) {
        if (text.contains("%time%")) {
            LocalTime time = LocalTime.now();
            text = TIME.matcher(text).replaceAll(time.getHour() + ":" + time.getMinute());
        }
        text = DATE.matcher(text).replaceAll(LocalDate.now().toString());
        text = RANDOM.matcher(text).replaceAll(String.valueOf(new Random().nextInt(100) + 1));

        return text;
    }/**
     * ГОВНО
     *//**
     * ГОВНО
     */
    static String generateResponse(String input) {
        double proximity = 0;
        String output = "Произошел сбой, небольшие технические шоколодки";
        for (Map.Entry<String, String> entry : DIAGLOG.entrySet()) {
            for (String word : entry.getKey().split("/")) {
                double similarity = COSINE.similarity(input, word);
                if(similarity <= proximity) {
                    continue;
                }
                proximity = similarity;
                output = entry.getValue();
            }
        }
        String[] rand = OR.split(output);
        return replacementKey(rand[new Random().nextInt(rand.length)]);
    }
}
