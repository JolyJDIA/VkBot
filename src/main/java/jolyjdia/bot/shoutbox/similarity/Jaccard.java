package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import jolyjdia.bot.shoutbox.similarity.interfaces.StringSimilarity;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jaccard extends ShingleBased implements StringSimilarity, StringDistance {

    public Jaccard(int k) {
        super(k);
    }

    public Jaccard() {

    }


    /**
     * Compute Jaccard index: |A inter B| / |A union B|.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The Jaccard index in the range [0, 1]
     * @throws NullPointerException if s1 OR s2 is null.
     */
    @Contract("null, _ -> fail; !null, null -> fail")
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

        Map<String, Integer> profile1 = getProfile(s1);
        Map<String, Integer> profile2 = getProfile(s2);


        Set<String> union = new HashSet<>();
        union.addAll(profile1.keySet());
        union.addAll(profile2.keySet());

        int inter = profile1.keySet().size() + profile2.keySet().size() - union.size();

        return 1.0 * inter / union.size();
    }


    /**
     * Distance is computed as 1 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1 - the Jaccard similarity.
     * @throws NullPointerException if s1 OR s2 is null.
     */
    @Override
    public final double distance(String s1, String s2) {
        return 1.0 - similarity(s1, s2);
    }
}
