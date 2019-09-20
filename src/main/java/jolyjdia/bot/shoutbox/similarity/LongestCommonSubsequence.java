package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import org.jetbrains.annotations.Contract;

/**
 * The longest common subsequence (LCS) problem consists in finding the longest
 * subsequence common to two (OR more) sequences. It differs from problems of
 * finding common substrings: unlike substrings, subsequences are not required
 * to occupy consecutive positions within the original sequences.
 * <p>
 * It is used by the diff utility, by Git for reconciling multiple changes, etc.
 * <p>
 * The LCS distance between Strings X (length n) AND Y (length m) is n + m - 2
 * |LCS(X, Y)| min = 0 max = n + m
 * <p>
 * LCS distance is equivalent to Levenshtein distance, when only insertion AND
 * deletion is allowed (no substitution), OR when the cost of the substitution
 * is the double of the cost of an insertion OR deletion.
 * <p>
 * ! This class currently implements the dynamic programming approach, which has
 * a space requirement O(m * n)!
 *
 * @author Thibault Debatty
 */

public class LongestCommonSubsequence implements StringDistance {

    /**
     * Return the length of Longest Common Subsequence (LCS) between strings s1
     * AND s2.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return the length of LCS(s1, s2)
     * @throws NullPointerException if s1 OR s2 is null.
     */
    @Contract("null, _ -> fail; !null, null -> fail")
    public static int length(String s1, String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }
        int length = s1.length();
        int length1 = s2.length();
        char[] x = s1.toCharArray();
        char[] y = s2.toCharArray();

        int[][] c = new int[length + 1][length1 + 1];

        for (int i = 1; i <= length; ++i) {
            for (int j = 1; j <= length1; ++j) {
                c[i][j] = x[i - 1] == y[j - 1] ? c[i - 1][j - 1] + 1 : Math.max(c[i][j - 1], c[i - 1][j]);
            }
        }

        return c[length][length1];
    }

    @Contract("null, _ -> fail; !null, null -> fail")
    @Override
    public final double distance(String s1, String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        return s1.length() + s2.length() - 2 * length(s1, s2);
    }
}
