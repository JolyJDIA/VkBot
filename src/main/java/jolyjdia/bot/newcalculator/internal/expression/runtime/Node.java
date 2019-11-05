package jolyjdia.bot.newcalculator.internal.expression.runtime;

import org.jetbrains.annotations.Contract;

public abstract class Node implements RValue {

    private final int position;

    @Contract(pure = true)
    Node(int position) {
        this.position = position;
    }

    @Contract(pure = true)
    @Override
    public final int getPosition() {
        return position;
    }
}
