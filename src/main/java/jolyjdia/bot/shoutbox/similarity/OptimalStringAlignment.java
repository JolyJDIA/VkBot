/*
 * The MIT License
 *
 * Copyright 2016 Thibault Debatty.
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
import org.jetbrains.annotations.Contract;

/**
 * Implementation of the the Optimal String Alignment (sometimes called the
 * restricted edit distance) variant of the Damerau-Levenshtein distance.
 * <p>
 * The difference between the two algorithms consists in that the Optimal String
 * Alignment algorithm computes the number of edit operations needed to make the
 * strings equal under the condition that no substring is edited more than once,
 * whereas Damerau-Levenshtein presents no such restriction.
 *
 * @author Michail Bogdanos
 */

public final class OptimalStringAlignment implements StringDistance {

    private static int min(
            int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    @Contract("null, _ -> fail; !null, null -> fail")
    @Override
    public double distance(String s1, String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        int n = s1.length();
        int m = s2.length();

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        // Create the distance jolyjdia.nn.jolyjdia.bot.generateText.neuralnetwork.matrix H[0 .. s1.length+1][0 .. s2.length+1]
        int[][] d = new int[n + 2][m + 2];

        //initialize top row AND leftmost column
        for (int i = 0; i <= n; ++i) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; ++j) {
            d[0][j] = j;
        }

        //fill the distance jolyjdia.nn.jolyjdia.bot.generateText.neuralnetwork.matrix
        int cost;

        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {

                //if s1[i - 1] = s2[j - 1] then cost = 0, else cost = 1
                cost = 1;
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cost = 0;
                }

                d[i][j] = min(
                        d[i - 1][j - 1] + cost, // substitution
                        d[i][j - 1] + 1, // insertion
                        d[i - 1][j] + 1 // deletion
                );

                //transposition check
                if (i > 1 && j > 1
                        && s1.charAt(i - 1) == s2.charAt(j - 2)
                        && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + cost);
                }
            }
        }

        return d[n][m];
    }
}
