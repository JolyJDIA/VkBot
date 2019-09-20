package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import org.jetbrains.annotations.Contract;

/**
 * The Levenshtein distance between two words is the minimum number of
 * single-character edits (insertions, deletions OR substitutions) required to
 * change one string into the other.
 *
 * @author Thibault Debatty
 */

public class Levenshtein implements StringDistance {

    @Contract("null, _, _ -> fail; !null, null, _ -> fail")
    public static double distance(String s1, String s2,
                                  int limit) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        if (s1.isEmpty()) {
            return s2.length();
        }

        if (s2.isEmpty()) {
            return s1.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[s2.length() + 1];
        int[] v1 = new int[s2.length() + 1];
        int[] vtemp;

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; ++i) {
            v0[i] = i;
        }

        for (int i = 0; i < s1.length(); ++i) {
            // calculate v1 (current row distances) from the previous row v0
            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            int minv1 = v1[0];

            // use formula to fill in the rest of the row
            for (int j = 0; j < s2.length(); ++j) {
                int cost = 1;
                if (s1.charAt(i) == s2.charAt(j)) {
                    cost = 0;
                }
                v1[j + 1] = Math.min(
                        v1[j] + 1,              // Cost of insertion
                        Math.min(
                                v0[j + 1] + 1,  // Cost of remove
                                v0[j] + cost)); // Cost of substitution

                minv1 = Math.min(minv1, v1[j + 1]);
            }

            if (minv1 >= limit) {
                return limit;
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            //System.arraycopy(v1, 0, v0, 0, v0.length);

            // Flip references to current AND previous row
            vtemp = v0;
            v0 = v1;
            v1 = vtemp;

        }

        return v0[s2.length()];
    }

    /**
     * Equivalent to distance(s1, s2, int.MAX_VALUE).
     */
    @Contract("null, _ -> fail; !null, null -> fail")
    @Override
    public final double distance(String s1, String s2) {
        return distance(s1, s2, Integer.MAX_VALUE);
    }
}
