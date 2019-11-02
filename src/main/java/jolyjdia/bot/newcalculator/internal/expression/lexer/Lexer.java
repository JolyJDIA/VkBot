package jolyjdia.bot.newcalculator.internal.expression.lexer;

import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Lexer {

    private final String expression;
    private int position;

    @Contract(pure = true)
    private Lexer(String expression) {
        this.expression = expression;
    }

    public static @NotNull List<Token> tokenize(String expression) throws LexerException {
        return new Lexer(expression).tokenize();
    }

    private static final DecisionTree operatorTree = new DecisionTree(null,
            '+', new DecisionTree("+",
            '=', new DecisionTree("+="),
            '+', new DecisionTree("++")
    ),
            '-', new DecisionTree("-",
            '=', new DecisionTree("-="),
            '-', new DecisionTree("--")
    ),
            '*', new DecisionTree("*",
            '=', new DecisionTree("*="),
            '*', new DecisionTree("**")
    ),
            '/', new DecisionTree("/",
            '=', new DecisionTree("/=")
    ),
            '%', new DecisionTree("%",
            '=', new DecisionTree("%=")
    ),
            '^', new DecisionTree("^",
            '=', new DecisionTree("^=")
    ),
            '=', new DecisionTree("=",
            '=', new DecisionTree("==")
    ),
            '!', new DecisionTree("!",
            '=', new DecisionTree("!=")
    ),
            '<', new DecisionTree("<",
            '<', new DecisionTree("<<"),
            '=', new DecisionTree("<=")
    ),
            '>', new DecisionTree(">",
            '>', new DecisionTree(">>"),
            '=', new DecisionTree(">=")
    ),
            '&', new DecisionTree(null, // not implemented
            '&', new DecisionTree("&&")
    ),
            '|', new DecisionTree(null, // not implemented
            '|', new DecisionTree("||")
    ),
            '~', new DecisionTree("~",
            '=', new DecisionTree("~=")
    ));

    private static final String OPERATORS = "()";
    private static final Pattern numberPattern = Pattern.compile("^([0-9]*(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?)");
    private static final Pattern identifierPattern = Pattern.compile("^([A-Za-z][0-9A-Za-z_]*)");

    private @NotNull List<Token> tokenize() throws LexerException {
        List<Token> tokens = new ArrayList<>();

        do {
            skipWhitespace();
            if (position >= expression.length()) {
                break;
            }

            Token token = evaluate(operatorTree.subTrees, operatorTree.tokenName, position);
            if (token != null) {
                tokens.add(token);
                continue;
            }

            final char ch = peek();

            if (containsChar(ch)) {
                tokens.add(new CharacterToken(position++, ch));
                continue;
            }

            final Matcher numberMatcher = numberPattern.matcher(expression.substring(position));
            if (numberMatcher.lookingAt()) {
                String numberPart = numberMatcher.group(1);
                if (!numberPart.isEmpty()) {
                    try {
                        tokens.add(new NumberToken(position, Double.parseDouble(numberPart)));
                    } catch (NumberFormatException e) {
                        throw new LexerException(position, "Number parsing failed", e);
                    }

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
                    continue;
                }
            }

            throw new LexerException(position, "Unknown character '" + ch);
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

    private static boolean containsChar(char codePoint) {
        for (int i = 0; i < OPERATORS.length(); i++) {
            if (OPERATORS.charAt(i) == codePoint) {
                return true;
            }
        }
        return false;
    }
    private @Nullable Token evaluate(Map<Character, DecisionTree> subTrees, String tokenName, int startPosition) {
        if (position < expression.length()) {
            final char next = peek();

            final DecisionTree subTree = subTrees.get(next);
            if (subTree != null) {
                ++position;
                final Token subTreeResult = evaluate(subTree.subTrees, subTree.tokenName, startPosition);
                if (subTreeResult != null) {
                    return subTreeResult;
                }
                --position;
            }
        }

        if (tokenName == null) {
            return null;
        }

        return new OperatorToken(startPosition, tokenName);
    }

    private static final class DecisionTree {
        private final String tokenName;
        private final Map<Character, DecisionTree> subTrees = new HashMap<>();

        private DecisionTree(String tokenName, @NotNull Object... args) {
            this.tokenName = tokenName;

            if (args.length % 2 != 0) {
                throw new UnsupportedOperationException("You need to pass an even number of arguments.");
            }

            for (int i = 0; i < args.length; i += 2) {
                if (!(args[i] instanceof Character)) {
                    throw new UnsupportedOperationException("Argument #" + i + " expected to be 'Character', not '" + args[i].getClass().getName() + "'.");
                }
                if (!(args[i + 1] instanceof DecisionTree)) {
                    throw new UnsupportedOperationException("Argument #" + (i + 1) + " expected to be 'DecisionTree', not '" + args[i + 1].getClass().getName() + "'.");
                }

                Character next = (Character) args[i];
                DecisionTree subTree = (DecisionTree) args[i + 1];

                subTrees.put(next, subTree);
            }
        }
    }
}
