package jolyjdia.bot.calculate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RelatedParentheses {
    private final List<String> userInpList;

    @Contract(pure = true)
    public RelatedParentheses(List<String> userInpList) {
        this.userInpList = userInpList;
    }

    @NotNull
    public final Map<Integer, Integer> evaluateRelations() {
        ArrayList<Integer> openingParenthesis = new ArrayList<>();
        Map<Integer, Integer> relationships = new TreeMap<>();
        
        for (int i = 0; i < userInpList.size(); i++) {
            if (userInpList.get(i).equals("(")) {
                openingParenthesis.add(i);
            } else if (userInpList.get(i).equals(")") && !openingParenthesis.isEmpty()) {
                relationships.put(openingParenthesis.get( openingParenthesis.size()-1 ), i);
                openingParenthesis.remove(openingParenthesis.size()-1);
            }
        }
        return relationships;
    }
}