package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import jolyjdia.bot.shoutbox.similarity.interfaces.StringSimilarity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * The Jaro–Winkler distance metric is designed AND best suited for short
 * strings such as person names, AND to detect typos; it is (roughly) a
 * variation of Damerau-Levenshtein, where the substitution of 2 close
 * characters is considered less important then the substitution of 2 characters
 * that a far from each other.
 * Jaro-Winkler was developed in the area of record linkage (duplicate
 * detection) (Winkler, 1990). It returns a value in the interval [0.0, 1.0].
 * The distance is computed as 1 - Jaro-Winkler similarity.
 *
 * @author Thibault Debatty
 */
public class JaroWinkler implements StringDistance, StringSimilarity {

    private static final double DEFAULT_THRESHOLD = 0.7;
    private static final int THREE = 3;
    private static final double JW_COEF = 0.1;
    private final double threshold;

    /**
     * Instantiate with default threshold (0.7).
     */
    public JaroWinkler() {
        this.threshold = DEFAULT_THRESHOLD;
    }

    /**
     * Instantiate with given threshold to determine when Winkler bonus should
     * be used.
     * Set threshold to a negative value to get the Jaro distance.
     *
     * @param threshold
     */
    public JaroWinkler(double threshold) {
        this.threshold = threshold;
    }

    @NotNull
    @Contract("_, _ -> new")
    private static int[] matches(@NotNull String s1, @NotNull String s2) {
        String max;
        String min;
        if (s1.length() > s2.length()) {
            max = s1;
            min = s2;
        } else {
            max = s2;
            min = s1;
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] ints = new int[min.length()];
        Arrays.fill(ints, -1);
        boolean[] booleans = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); ++mi) {
            char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0),
                 xn = Math.min(mi + range + 1, max.length());
                 xi < xn;
                 ++xi) {
                if (!booleans[xi] && c1 == max.charAt(xi)) {
                    ints[mi] = xi;
                    booleans[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        int si = 0;
        for (int i = 0; i < min.length(); ++i) {
            if (ints[i] != -1) {
                ms1[si] = min.charAt(i);
                ++si;
            }
        }
        int si1 = 0;
        for (int i = 0; i < max.length(); ++i) {
            if (booleans[i]) {
                ms2[si1] = max.charAt(i);
                ++si1;
            }
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; ++mi) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (s1.charAt(mi) == s2.charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }
        return new int[]{matches, transpositions / 2, prefix, max.length()};
    }

    /**
     * Returns the current value of the threshold used for adding the Winkler
     * bonus. The default value is 0.7.
     *
     * @return the current value of the threshold
     */
    @Contract(pure = true)
    public final double getThreshold() {
        return threshold;
    }

    /**
     * Compute Jaro-Winkler similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The Jaro-Winkler similarity in the range [0, 1]
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

        int[] mtp = matches(s1, s2);
        float m = mtp[0];
        if (m == 0) {
            return 0f;
        }
        double j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m))
                / THREE;
        double jw = j;

        if (j > threshold) {
            jw = j + Math.min(JW_COEF, 1.0 / mtp[THREE]) * mtp[2] * (1 - j);
        }
        return jw;
    }

    /**
     * Return 1 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1 - similarity.
     * @throws NullPointerException if s1 OR s2 is null.
     */
    @Override
    public final double distance(String s1, String s2) {
        return 1.0 - similarity(s1, s2);
    }
}
