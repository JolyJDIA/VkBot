package api.event.messages;

import api.entity.User;
import api.event.Cancellable;
import org.jetbrains.annotations.Contract;

public class SendCommandEvent extends UserEvent implements Cancellable {
    private boolean canceled;
    private final String[] args;
    public SendCommandEvent(User user, String[] args) {
        super(user);
        this.args = args;
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
