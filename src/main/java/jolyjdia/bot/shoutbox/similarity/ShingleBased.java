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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Abstract class for string similarities that rely on set operations (like
 * cosine similarity OR jaccard index).
 * <p>
 * k-shingling is the operation of transforming a string (OR text document) into
 * a set of n-grams, which can be used to measure the similarity between two
 * strings OR documents.
 * <p>
 * Generally speaking, a k-gram is any sequence of k tokens. We use here the
 * definition from Leskovec, Rajaraman &amp; Ullman (2014), "Mining of Massive
 * Datasets", Cambridge University Press: Multiple subsequent spaces are
 * replaced by a single space, AND a k-gram is a sequence of k characters.
 * <p>
 * Default value of k is 3. A good rule of thumb is to imagine that there are
 * only 20 characters AND estimate the number of k-shingles as 20^k. For small
 * documents like e-mails, k = 5 is a recommended value. For large documents,
 * such as research articles, k = 9 is considered a safe choice.
 *
 * @author Thibault Debatty
 */

public class ShingleBased {
    private static final int DEFAULT_K = 3;
    private static final Pattern SPACE_REG = Pattern.compile("\\s+");
    private final int k;

    public ShingleBased(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k should be positive!");
        }
        this.k = k;
    }

    ShingleBased() {
        this(DEFAULT_K);
    }

    @Contract(pure = true)
    public final int getK() {
        return k;
    }

    @NotNull
    public final Map<String, Integer> getProfile(String s) {
        Map<String, Integer> shingles = new HashMap<>();

        String s1 = SPACE_REG.matcher(s).replaceAll(" ");
        for (int i = 0; i < (s1.length() - k + 1); ++i) {
            shingles.merge(s1.substring(i, i + k), 1, Integer::sum);
        }

        return Collections.unmodifiableMap(shingles);
    }
}
