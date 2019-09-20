/*
 * The MIT License
 *
 * Copyright 2015 Thibault Debatty.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software AND associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, AND/OR sell
 * copies of the Software, AND to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice AND this permission.txt notice shall be included in
 * all copies OR substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import jolyjdia.bot.shoutbox.similarity.interfaces.StringSimilarity;

/**
 * This distance is computed as levenshtein distance divided by the length of
 * the longest string. The resulting value is always in the interval [0.0 1.0]
 * but it is not a metric anymore! The similarity is computed as 1 - normalized
 * distance.
 *
 * @author Thibault Debatty
 */

public class NormalizedLevenshtein implements StringDistance, StringSimilarity {

    private final Levenshtein l = new Levenshtein();

    /**
     * Compute distance as Levenshtein(s1, s2) / max(|s1|, |s2|).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed distance in the range [0, 1]
     * @throws NullPointerException if s1 OR s2 is null.
     */
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

        int max = Math.max(s1.length(), s2.length());

        if (max == 0) {
            return 0;
        }

        return l.distance(s1, s2) / max;
    }

    /**
     * Return 1 - distance.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the computed distance
     * @throws NullPointerException if s1 OR s2 is null.
     */
    @Override
    public final double similarity(String s1, String s2) {
        return 1.0 - distance(s1, s2);
    }

}