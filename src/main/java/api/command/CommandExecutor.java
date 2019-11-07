package api.command;

import api.storage.User;

@FunctionalInterface
public interface CommandExecutor {
    void execute(User sender, String[] args);
    default void execute(int peerId, int userId, String[] args) {}
}
