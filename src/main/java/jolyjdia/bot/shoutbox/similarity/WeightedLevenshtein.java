package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import org.jetbrains.annotations.Contract;

public class WeightedLevenshtein implements StringDistance {
    private final CharacterSubstitutionInterface charsub;
    private final CharacterInsDelInterface charchange;

    public WeightedLevenshtein(CharacterSubstitutionInterface charsub) {
        this(charsub, null);
    }

    public WeightedLevenshtein(CharacterSubstitutionInterface charsub,
                               CharacterInsDelInterface charchange) {
        this.charsub = charsub;
        this.charchange = charchange;
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

        if (s1.isEmpty()) {
            return s2.length();
        }

        if (s2.isEmpty()) {
            return s1.length();
        }

        // create two work vectors of floating point (i.e. weighted) distances
        double[] v0 = new double[s2.length() + 1];
        double[] v1 = new double[s2.length() + 1];
        double[] vtemp;

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s1
        // the distance is the cost of inserting each character of s2
        v0[0] = 0;
        for (int i = 1; i < v0.length; ++i) {
            v0[i] = v0[i - 1] + insertionCost(s2.charAt(i - 1));
        }

        for (int i = 0; i < s1.length(); ++i) {
            char s1i = s1.charAt(i);
            double v = deletionCost(s1i);

            // calculate v1 (current row distances) from the previous row v0
            // first element of v1 is A[i+1][0]
            // Edit distance is the cost of deleting characters from s1
            // to match empty t.
            v1[0] = v0[0] + v;

            double minv1 = v1[0];

            // use formula to fill in the rest of the row
            for (int j = 0; j < s2.length(); ++j) {
                char s2j = s2.charAt(j);
                double cost = 0;
                if (s1i != s2j) {
                    cost = charsub.cost(s1i, s2j);
                }
                double v2 = insertionCost(s2j);
                v1[j + 1] = Math.min(
                        v1[j] + v2, // Cost of insertion
                        Math.min(
                                v0[j + 1] + v, // Cost of deletion
                                v0[j] + cost)); // Cost of substitution

                minv1 = Math.min(minv1, v1[j + 1]);
            }

            if (minv1 >= Double.MAX_VALUE) {
                return Double.MAX_VALUE;
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


    private double insertionCost(char c) {
        return charchange == null ? 1.0 : charchange.insertionCost(c);
    }

    private double deletionCost(char c) {
        return charchange == null ? 1.0 : charchange.deletionCost(c);
    }
}
