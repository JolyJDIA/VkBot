package jolyjdia.bot.newcalculator.internal.expression.parser;

import jolyjdia.bot.newcalculator.internal.expression.Expression;
import jolyjdia.bot.newcalculator.internal.expression.ExpressionException;
import jolyjdia.bot.newcalculator.internal.expression.Identifiable;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.IdentifierToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.NumberToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.OperatorToken;
import jolyjdia.bot.newcalculator.internal.expression.lexer.tokens.Token;
import jolyjdia.bot.newcalculator.internal.expression.runtime.Constant;
import jolyjdia.bot.newcalculator.internal.expression.runtime.Function;
import jolyjdia.bot.newcalculator.internal.expression.runtime.Functions;
import jolyjdia.bot.newcalculator.internal.expression.runtime.RValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
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
    }

    private final List<? extends Token> tokens;
    private int position;
    private final Expression expression;

    @Contract(pure = true)
    private Parser(List<? extends Token> tokens, Expression expression) {
        this.tokens = tokens;
        this.expression = expression;
    }

    public static @NotNull RValue parse(List<? extends Token> tokens, Expression expression) throws ExpressionException {
        return new Parser(tokens, expression).parse();
    }

    private @NotNull RValue parse() throws ExpressionException {
        final RValue ret = parseStatements();
        if (position < tokens.size()) {
            throw new ExpressionException(-1, "Дополнительный токен в конце ввода: ");
        }

        ret.bindVariables(expression);
        return ret;
    }

    private RValue parseStatements() throws ExpressionException {
        List<RValue> statements = new ArrayList<>();
        if (position < tokens.size()) {
            statements.add(parseExpression());
        }
        if (statements.size() == 1) {
            return statements.get(0);
        } else {
            throw new IllegalStateException("Unexpected value: " + statements.size());
        }
    }

    private @NotNull RValue parseExpression() throws ExpressionException {
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
                        final RValue variable = Expression.getVariable(identifierToken.value);
                        halfProcessed.add(Objects.requireNonNullElseGet(variable, () -> new UnboundVariable(identifierToken.getPosition(), identifierToken.value)));
                    }
                    expressionStart = false;
                    break;

                case '(':
                    halfProcessed.add(parseBracket());
                    expressionStart = false;
                    break;
                case ',':
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
        return ParserProcessors.processExpression(halfProcessed);
    }

    private Token peek() {
        if (position >= tokens.size()) {
            return new NullToken(tokens.get(tokens.size() - 1).getPosition() + 1);
        }
        return tokens.get(position);
    }

    private @NotNull Function parseFunctionCall(IdentifierToken identifierToken) throws ExpressionException {
        consumeCharacter('(');

        if (peek().id() == ')') {
            ++position;
            return Functions.getFunction(identifierToken.getPosition(), identifierToken.value);
        }
        List<RValue> args = new ArrayList<>();

        loop: while (true) {
            args.add(parseExpression());

            final Token current = peek();
            ++position;

            switch (current.id()) {
                case ',':
                    continue;
                case ')':
                    break loop;
            }
        }
        return Functions.getFunction(identifierToken.getPosition(), identifierToken.value, args.toArray(new RValue[0]));
    }

    private @NotNull RValue parseBracket() throws ExpressionException {
        consumeCharacter('(');
        final RValue ret = parseExpression();
        consumeCharacter(')');
        return ret;
    }


    private void assertCharacter(@NonNls char character) throws ExpressionException {
        final Token next = peek();
        if (next.id() != character) {
            throw new ExpressionException(next.getPosition(), "Ожидаемый " + character);
        }
    }
    private void consumeCharacter(char character) throws ExpressionException {
        assertCharacter(character);
        ++position;
    }
}
