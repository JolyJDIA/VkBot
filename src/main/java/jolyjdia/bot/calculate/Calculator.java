package jolyjdia.bot.calculate;

import jolyjdia.bot.calculate.basic.BasicFunction;
import jolyjdia.bot.calculate.basic.Computable;
import jolyjdia.bot.calculate.basic.Tree;
import jolyjdia.bot.calculate.token.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\A(\\d+(\\.\\d*)?).*");
    private static final Pattern COMPILE = Pattern.compile("\\s");
    private static final Pattern PATTERN = Pattern.compile("\\A-");
    private static final Pattern COMPILE1 = Pattern.compile(",-");
    private static final Pattern COMPILE2 = Pattern.compile("\\(-");
    private static final Pattern COMPILE3 = Pattern.compile("\\+-");
    private static final Pattern COMPILE4 = Pattern.compile("-\\+");
    private static final List<Token> TOKEN_LIST = new ArrayList<>();

    static {
        for (TokenOperator.EnumOperator type : TokenOperator.EnumOperator.values()) {
            registerToken(new TokenOperator(type));
        }
        for (BasicFunction.EnumFunction type : BasicFunction.EnumFunction.values()) {
            registerToken(new BasicFunction(type));
        }
        registerToken(new TokenConstant("e", Math.E));
        registerToken(new TokenConstant("pi", Math.PI));
        registerToken(Token.BRACKET_LEFT);
        registerToken(Token.BRACKET_RIGHT);
    }
    public static void registerToken(@NotNull Token token) {
        TOKEN_LIST.add(token);
    }
    private String expression;
    public Calculator(String expression) {
        expression = COMPILE3.matcher(expression).replaceAll("-");
        expression = COMPILE4.matcher(expression).replaceAll("-");
        expression = COMPILE.matcher(expression).replaceAll("");
        expression = PATTERN.matcher(expression).replaceAll("0-");
        expression = COMPILE2.matcher(expression).replaceAll("(0-");
        expression = COMPILE1.matcher(expression).replaceAll(",0-");
        expression = expression.toLowerCase(Locale.ENGLISH);
        this.expression = expression;
    }

    @NotNull
    public final String evaluate() {
        List<Token> tokens = tokenize();
        List<Token> rpnSequence = null;
        if (tokens != null) {
            Tree<Token> tree = buildSyntaxTree(tokens);
            if (tree != null) {
                rpnSequence = tree.sequentializePostOrder();
            }
        }
        if (rpnSequence != null) {
            return calculateRPN(rpnSequence);
        }
        return "";

    }

    @Nullable
    private List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (!expression.isEmpty()) {

            Matcher numberMatcher = NUMBER_PATTERN.matcher(expression);

            if (numberMatcher.find()) {
                String found = numberMatcher.group(1);
                expression = expression.substring(found.length());
                double value = Double.parseDouble(found);
                tokens.add(new TokenValue(value));
            }
            if (expression.isEmpty()) {
                break;
            }
            Token longestToken = null;
            for (Token token : TOKEN_LIST) {
                if (expression.startsWith(token.getNotation())) {
                    if (longestToken == null || longestToken.getNotation().length() < token.getNotation().length()) {
                        longestToken = token;
                    }
                }
            }
            if (longestToken == null) {
                return null;
            }
            expression = expression.substring(longestToken.getNotation().length());
            tokens.add(longestToken);
        }
        return tokens;
    }

    @Nullable
    private static Tree<Token> buildSyntaxTree(@NotNull List<? extends Token> list) {
        Tree.Node<Token> headNode = new Tree.Node<>(null, null);
        for (Token curToken : list) {
            if (headNode.getData() == null) {
                headNode.setData(curToken);
                continue;
            }
            if (curToken == Token.BRACKET_LEFT) {
                if (headNode.getData() instanceof TokenComputable || headNode.getData() == Token.BRACKET_LEFT) {
                    Tree.Node<Token> child = new Tree.Node<>(headNode, curToken);
                    headNode.addChild(child);
                    headNode = child;
                } else {
                    return null;
                }
            } else if (curToken == Token.BRACKET_RIGHT) {
                while (headNode.getData() != Token.BRACKET_LEFT) {
                    headNode = headNode.getParent();
                    if (headNode == null) {
                        return null;
                    }
                }
                if (headNode.getParent() != null && headNode.getParent().getData() instanceof TokenComputable
                        && ((Computable) headNode.getParent().getData()).noInfix()) {
                    Tree.Node<Token> parent = headNode.getParent();
                    parent.absorbNode(headNode);
                    headNode = parent;
                } else {
                    headNode.setData(Token.BRACKET_COMPLETE);
                }
            } else if (curToken instanceof TokenValue) {
                if (headNode.getData() == Token.BRACKET_LEFT) {
                    Tree.Node<Token> childNode = new Tree.Node<>(headNode, curToken);
                    headNode.addChild(childNode);
                    headNode = childNode;
                } else if (headNode.getData() instanceof TokenComputable) {
                    Tree.Node<Token> childNode = new Tree.Node<>(headNode, curToken);
                    headNode.addChild(childNode);
                } else {
                    return null;
                }
            } else if (curToken instanceof TokenComputable) {
                if (headNode.getData() instanceof TokenValue) {
                    Tree.Node<Token> parentFunctionNode = new Tree.Node<>(null, curToken);
                    headNode.insertParent(parentFunctionNode);
                    headNode = parentFunctionNode;
                } else if (headNode.getData() == Token.BRACKET_LEFT) {
                    Tree.Node<Token> childFunctionNode = new Tree.Node<>(null, curToken);
                    headNode.addChild(childFunctionNode);
                    headNode = childFunctionNode;
                } else if (headNode.getData() == Token.BRACKET_COMPLETE) {
                    headNode.setData(curToken);
                } else if (headNode.getData() instanceof TokenComputable) {
                    TokenComputable headFunction = (TokenComputable) headNode.getData();
                    TokenComputable curFunction = (TokenComputable) curToken;
                    if (headFunction.getPrecedence() < curFunction.getPrecedence()) {
                        Tree.Node<Token> childFunctionNode = new Tree.Node<>(null, curFunction);
                        if (curFunction.noInfix()) {
                            continue;
                        }
                        Tree.Node<Token> childValueNode = headNode.getChildren().get(headNode.getChildren().size() - 1);
                        headNode.getChildren().remove(childValueNode);
                        childFunctionNode.addChild(childValueNode);
                        headNode.addChild(childFunctionNode);
                        headNode = childFunctionNode;
                        continue;
                    }
                    Tree.Node<Token> parentFunctionNode = new Tree.Node<>(null, curFunction);
                    headNode.insertParent(parentFunctionNode);
                    headNode = parentFunctionNode;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        Tree<Token> tree = new Tree<>(null);
        while (headNode.getParent() != null) {
            headNode = headNode.getParent();
        }
        tree.setRoot(headNode);
        return tree;
    }

    @NotNull
    @NonNls
    private static String calculateRPN(@NotNull List<? extends Token> rpnSequence) {
        Stack<Token> calcStack = new Stack<>();
        for (Token node : rpnSequence) {
            if (node instanceof TokenValue) {
                calcStack.push(node);
            } else if (node instanceof TokenComputable) {
                TokenComputable function = (TokenComputable) node;
                if (calcStack.size() < function.getArguments()) {
                    return "";
                }

                double[] params = new double[function.getArguments()];
                for (int i = function.getArguments() - 1; i >= 0; i--) {
                    Token paramNode = calcStack.pop();
                    if (!(paramNode instanceof TokenValue)) {
                        return "";
                    }
                    params[i] = ((TokenValue) paramNode).getValue();
                }

                double result = function.compute(params);
                calcStack.push(new TokenValue(result));
            }
        }
        if (calcStack.size() != 1) {
            return "";
        }
        Token node = calcStack.pop();
        if (!(node instanceof TokenValue)) {
            return "";
        }
        return String.valueOf(((TokenValue) node).getValue());
    }
}
