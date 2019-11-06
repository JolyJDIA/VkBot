package jolyjdia.bot.newcalculator.internal.expression.lexer;

import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Lexer {
    private static final char[] OPERATORS = {'+', '-', '*', '/', '^', '%', '!', '~'};
    private static final char[] CHARS = {'(', ')', ','};
    private static final Pattern numberPattern = Pattern.compile("^([0-9]*(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?)");
    private static final Pattern identifierPattern = Pattern.compile("^([A-Za-z][0-9A-Za-z_]*)");
    private final String expression;
    private int position;

    @Contract(pure = true)
    private Lexer(String expression) {
        this.expression = expression;
    }

    public static @NotNull List<Token> tokenize(String expression) {
        return new Lexer(expression).tokenize();
    }

    private @NotNull List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        do {
            skipWhitespace();
            char peek = peek();
            if(containsOper(peek)) {
                tokens.add(new OperatorToken(position++, String.valueOf(peek)));
                continue;
            } else if (containsChar(peek)) {
                tokens.add(new CharacterToken(position++, peek));
                continue;
            }
            final Matcher numberMatcher = numberPattern.matcher(expression.substring(position));
            if (numberMatcher.lookingAt()) {
                String numberPart = numberMatcher.group(1);
                if (!numberPart.isEmpty()) {
                    tokens.add(new NumberToken(position, Double.parseDouble(numberPart)));
                    position += numberPart.length();
                    continue;
                }
            }
            final Matcher identifierMatcher = identifierPattern.matcher(expression.substring(position));
            if (identifierMatcher.lookingAt()) {
                String identifierPart = identifierMatcher.group(1);
                if (!identifierPart.isEmpty()) {
                    tokens.add(new IdentifierToken(position, identifierPart));
                    position += identifierPart.length();
                }
            }
        } while (position < expression.length());
        return tokens;
    }
    @Contract(pure = true)
    private char peek() {
        return expression.charAt(position);
    }

    private void skipWhitespace() {
        while (position < expression.length() && Character.isWhitespace(peek())) {
            ++position;
        }
    }

    @Contract(pure = true)
    private static boolean containsChar(char point) {
        for (char c : CHARS) {
            if (c == point) {
                return true;
            }
        }
        return false;
    }
    @Contract(pure = true)
    private static boolean containsOper(char codePoint) {
        for (char c : OPERATORS) {
            if (c == codePoint) {
                return true;
            }
        }
        return false;
    }
}
