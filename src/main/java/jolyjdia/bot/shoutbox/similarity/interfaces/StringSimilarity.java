package jolyjdia.bot.shoutbox.similarity.interfaces;

@FunctionalInterface
public interface StringSimilarity {
    double similarity(String s1, String s2);
}
