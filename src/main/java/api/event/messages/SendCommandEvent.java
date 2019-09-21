package api.event.messages;

import api.event.Cancellable;
import api.event.Event;
import api.storage.User;
import org.jetbrains.annotations.Contract;

public class SendCommandEvent implements Event, Cancellable {
    private boolean canceled;
    private final String[] args;
    private final User user;
    @Contract(pure = true)

    public SendCommandEvent(User user, String[] args) {
        this.user = user;
        this.args = args;
    }
    @Contract(pure = true)
    public final User getUser() {
        return user;
    }

    @Contract(pure = true)
    @Override
    public final boolean isCancelled() {
        return canceled;
    }

    @Override
    public final void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @Contract(pure = true)
    public final String[] getArguments() {
        return args;
    }
}
