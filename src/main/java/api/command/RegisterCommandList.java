package api.command;

import api.command.defaults.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class RegisterCommandList {
    private final Set<Command> commands = new HashSet<>();//Классы уникальные

    public RegisterCommandList() {
        registerCommand(new HelpCommand());
        registerCommand(new ReloadCommand());
        registerCommand(new RankCommand());
        registerCommand(new SetPrefixCommand());
        registerCommand(new SetSuffixCommand());
        registerCommand(new EditTitleChatCommand());
        registerCommand(new UpTimeCommand());
        registerCommand(new TickPerSecondCommand());
        registerCommand(new BroadcastMessageCommand());
        registerCommand(new CalendarCommand());
        registerCommand(new InfoUserCommand());
        registerCommand(new VersionCommand());
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    @Contract(pure = true)
    public Set<Command> getRegisteredCommands() {
        return commands;
    }
    public void unregisterAll() {
        commands.clear();
    }

    public void registerAll(@NotNull Iterable<? extends Command> iterable) {
        iterable.forEach(commands::add);
    }
}