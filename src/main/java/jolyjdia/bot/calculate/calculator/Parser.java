package jolyjdia.bot.calculate.calculator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Parser {
    private final ArrayList<String> tokens = new ArrayList<>();

    private final StringBuilder tokenBuilder = new StringBuilder();
    public static final String OPERATORS = "+-*/^()";


    final ArrayList<String> parse(@NotNull String expression) {
        tokens.clear();
        if(expression.charAt(0) == '-') {
            tokenBuilder.append('0');
            tokenEnd();
        }
        for (int i = 0; i < expression.length(); i++) {
            int codePoint = expression.codePointAt(i);

            Parser.SymbolType type = getSymbolType(codePoint) ;
            if(type == Parser.SymbolType.SYMBOL) {
                tokenBuilder.appendCodePoint(codePoint);
            } else {
                tokenEnd();
                tokenBuilder.appendCodePoint(codePoint);
                tokenEnd();
            }
        }
        tokenEnd();
        return tokens;
    }

    private void tokenEnd() {
        if (tokenBuilder.length() > 0) {
            tokens.add(tokenBuilder.toString());

            tokenBuilder.setLength(0);
        }
    }

    private static Parser.SymbolType getSymbolType(int codePoint) {
        return containsCodePoint(codePoint) ? Parser.SymbolType.OPERATOR : Parser.SymbolType.SYMBOL;
    }

    private static boolean containsCodePoint(int codePoint) {
        for (int i = 0; i < OPERATORS.length(); i++) {
            if (OPERATORS.codePointAt(i) == codePoint) {
                return true;
            }
        }
        return false;
    }
    enum SymbolType {
        OPERATOR, SYMBOL
    }
}
