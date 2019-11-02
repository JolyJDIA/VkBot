package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.Identifiable;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.IdentifierToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.NumberToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.OperatorToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.expression.runtime.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class Parser {
    private static final class NullToken extends Token {
        private NullToken(int position) {
            super(position);
        }

        @Contract(pure = true)
        @Override
        public char id() {
            return '\0';
        }

        @Override
        @Contract(pure = true)
        public @NotNull String toString() {
            return "NullToken";
        }
    }

    private final List<? extends Token> tokens;
    private int position;
    private final Expression expression;

    @Contract(pure = true)
    private Parser(List<? extends Token> tokens, Expression expression) {
        this.tokens = tokens;
        this.expression = expression;
    }

    public static @NotNull RValue parse(List<? extends Token> tokens, Expression expression) throws ParserException {
        return new Parser(tokens, expression).parse();
    }

    private @NotNull RValue parse() throws ParserException {
        final RValue ret = parseStatements();
        if (position < tokens.size()) {
            final Token token = peek();
            throw new ParserException(token.getPosition(), "Extra token at the end of the input: " + token);
        }

        ret.bindVariables(expression);

        return ret;
    }

    private RValue parseStatements() throws ParserException {
        List<RValue> statements = new ArrayList<>();
        if (position < tokens.size()) {
            statements.add(parseExpression(true));
        }

        return switch (statements.size()) {
            case 0 -> new Sequence(peek().getPosition());
            case 1 -> statements.get(0);
            default -> new Sequence(peek().getPosition(), statements.toArray(new RValue[0]));
        };
    }

    private RValue parseExpression(boolean canBeEmpty) throws ParserException {
        LinkedList<Identifiable> halfProcessed = new LinkedList<>();
        boolean expressionStart = true;
        loop: while (position < tokens.size()) {
            final Token current = peek();

            switch (current.id()) {
                case '0':
                    halfProcessed.add(new Constant(((NumberToken) current).value));
                    ++position;
                    expressionStart = false;
                    break;

                case 'i':
                    final IdentifierToken identifierToken = (IdentifierToken) current;
                    ++position;

                    final Token next = peek();
                    if (next.id() == '(') {
                        halfProcessed.add(parseFunctionCall(identifierToken));
                    } else {
                        final RValue variable = expression.getVariable(identifierToken.value);
                        halfProcessed.add(Objects.requireNonNullElseGet(variable, () -> new UnboundVariable(identifierToken.getPosition(), identifierToken.value)));
                    }
                    expressionStart = false;
                    break;

                case '(':
                    halfProcessed.add(parseBracket());
                    expressionStart = false;
                    break;

                case ')':
                    break loop;
                case 'o':
                    if (expressionStart) {
                        halfProcessed.add(new UnaryOperator((OperatorToken) current));
                    } else {
                        halfProcessed.add(current);
                    }
                    ++position;
                    expressionStart = true;
                    break;
                default:
                    halfProcessed.add(current);
                    ++position;
                    expressionStart = false;
                    break;
            }
        }

        if (halfProcessed.isEmpty() && canBeEmpty) {
            return new Sequence(peek().getPosition());
        }
        return ParserProcessors.processExpression(halfProcessed);
    }

    private Token peek() {
        if (position >= tokens.size()) {
            return new NullToken(tokens.get(tokens.size() - 1).getPosition() + 1);
        }
        return tokens.get(position);
    }

    private @NotNull Function parseFunctionCall(IdentifierToken identifierToken) throws ParserException {
        consumeCharacter('(');

        try {
            if (peek().id() == ')') {
                ++position;
                return Functions.getFunction(identifierToken.getPosition(), identifierToken.value);
            }

            List<RValue> args = new ArrayList<>();
            args.add(parseExpression(false));
            ++position;
            return Functions.getFunction(identifierToken.getPosition(), identifierToken.value, args.toArray(new RValue[0]));
        } catch (NoSuchMethodException e) {
            throw new ParserException(identifierToken.getPosition(), "Function '" + identifierToken.value + "' not found", e);
        }
    }

    private RValue parseBracket() throws ParserException {
        consumeCharacter('(');
        final RValue ret = parseExpression(false);
        consumeCharacter(')');
        return ret;
    }


    private void assertCharacter(char character) throws ParserException {
        final Token next = peek();
        if (next.id() != character) {
            throw new ParserException(next.getPosition(), "Expected '" + character + '\'');
        }
    }
    private void consumeCharacter(char character) throws ParserException {
        assertCharacter(character);
        ++position;
    }
}
