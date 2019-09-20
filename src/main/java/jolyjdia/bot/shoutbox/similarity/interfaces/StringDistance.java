package jolyjdia.bot.shoutbox.similarity.interfaces;

@FunctionalInterface
public interface StringDistance {
    double distance(String s1, String s2);
}
