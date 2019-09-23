package api.event;

import org.jetbrains.annotations.Contract;

public enum EventPriority {
    LOWEST(-2),
    LOW(-1),
    NORMAL(0),
    HIGH(1),
    HIGHEST(2);

    private final int slot;

    @Contract(pure = true)
    EventPriority(int slot) {
        this.slot = slot;
    }

    @Contract(pure = true)
    public int getSlot() {
        return slot;
    }
}