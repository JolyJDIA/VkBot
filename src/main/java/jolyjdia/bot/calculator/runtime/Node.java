package jolyjdia.bot.calculator.runtime;

public abstract class Node implements RValue {

    private final int position;

    Node(int position) {
        this.position = position;
    }

    @Override
    public final int getPosition() {
        return position;
    }
}
