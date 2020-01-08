package jolyjdia.api.event.messages;

import jolyjdia.api.event.Cancellable;
import jolyjdia.api.event.Event;
import jolyjdia.api.storage.User;

public class SendCommandEvent implements Event, Cancellable {
    private boolean canceled;
    private final String[] args;
    private final User user;

    public SendCommandEvent(User user, String[] args) {
        this.user = user;
        this.args = args;
    }
    public final User getUser() {
        return user;
    }

    @Override
    public final boolean isCancelled() {
        return canceled;
    }

    @Override
    public final void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    public final String[] getArguments() {
        return args;
    }
}
