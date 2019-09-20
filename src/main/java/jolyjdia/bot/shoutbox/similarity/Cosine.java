package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import jolyjdia.bot.shoutbox.similarity.interfaces.StringSimilarity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Cosine extends ShingleBased implements StringDistance, StringSimilarity {

    public Cosine(int k) {
        super(k);
    }

    public Cosine() {
    }

    /**
     * Compute the norm L2 : sqrt(Sum_i( v_i²)).
     *
     * @param profile
     * @return L2 norm
     */
    private static double norm(@NotNull Map<String, Integer> profile) {
        double agg = 0;

        for (Map.Entry<String, Integer> entry : profile.entrySet()) {
            agg += 1.0 * entry.getValue() * entry.getValue();
        }

        return Math.sqrt(agg);
    }

    private static double dotProduct(@NotNull Map<String, Integer> profile1, @NotNull Map<String, Integer> profile2) {

        Map<String, Integer> profile21 = profile2;
        Map<String, Integer> profile11 = profile1;
        if (profile1.size() < profile2.size()) {
            profile21 = profile1;
            profile11 = profile2;
        }

        double agg = 0;
        for (Map.Entry<String, Integer> entry : profile21.entrySet()) {
            Integer i = profile11.get(entry.getKey());
            if (i == null) {
                continue;
            }
            agg += 1.0 * entry.getValue() * i;
        }

        return agg;
    }

    /**
     * {@inheritDoc}
     *
     * @param profile1
     * @param profile2
     * @return
     */
    public static double similarity(
            Map<String, Integer> profile1,
            Map<String, Integer> profile2) {

        return dotProduct(profile1, profile2)
                / (norm(profile1) * norm(profile2));
    }

    @Override
    public final double similarity(String s1, String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 1;
        }

        if (s1.length() < getK() || s2.length() < getK()) {
            return 0;
        }

        Map<String, Integer> profile1 = getProfile(s1);
        Map<String, Integer> profile2 = getProfile(s2);

        return dotProduct(profile1, profile2)
                / (norm(profile1) * norm(profile2));
    }

    /**
     * Return 1.0 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the cosine similarity in the range [0, 1]
     * @throws NullPointerException if s1 OR s2 is null.
     */
    @Override
    public final double distance(String s1, String s2) {
        return 1.0 - similarity(s1, s2);
    }

}
