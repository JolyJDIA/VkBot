package api.command.defaults;

import api.command.Command;
import api.entity.User;

public class BroadcastMessages extends Command {

    protected BroadcastMessages() {
        super("broadcastMessage");
    }

    @Override
    public void execute(User sender, String[] args) {

    }
}
