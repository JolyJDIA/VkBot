package api.event;

import org.jetbrains.annotations.Contract;

public enum EventPriority {
    LOWEST((byte)-2),
    LOW((byte)-1),
    NORMAL((byte)0),
    HIGH((byte)1),
    HIGHEST((byte)2);

    private final byte slot;

    @Contract(pure = true)
    EventPriority(byte slot) {
        this.slot = slot;
    }

    @Contract(pure = true)
    public byte getSlot() {
        return slot;
    }
}