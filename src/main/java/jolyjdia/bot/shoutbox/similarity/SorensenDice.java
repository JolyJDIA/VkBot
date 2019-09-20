package jolyjdia.bot.shoutbox.similarity;

import jolyjdia.bot.shoutbox.similarity.interfaces.StringDistance;
import jolyjdia.bot.shoutbox.similarity.interfaces.StringSimilarity;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SorensenDice extends ShingleBased implements StringDistance, StringSimilarity {

    public SorensenDice(int k) {
        super(k);
    }

    public SorensenDice() {
    }

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

        int inter = 0;

        for (String key : union) {
            if (profile1.containsKey(key) && profile2.containsKey(key)) {
                inter++;
            }
        }

        return 2.0 * inter / (profile1.size() + profile2.size());
    }

    @Override
    public final double distance(String s1, String s2) {
        return 1 - similarity(s1, s2);
    }
}
