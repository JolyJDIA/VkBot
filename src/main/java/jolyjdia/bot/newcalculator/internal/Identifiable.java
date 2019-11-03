package jolyjdia.bot.newcalculator.internal;

public interface Identifiable {

    /**
     * Returns a character that helps identify the token, pseudo-token or invokable in question.
     *
     * <pre>
     * Tokens:
     * i - IdentifierToken
     * 0 - NumberToken
     * o - OperatorToken
     * \0 - NullToken
     * CharacterTokens are returned literally
     *
     * PseudoTokens:
     * p - UnaryOperator
     * V - UnboundVariable
     *
     * Nodes:
     * c - Constant
     * v - Variable
     * f - Function
     * l - LValueFunction
     * s - Sequence
     * </pre>
     */
    char id();

    int getPosition();

}
