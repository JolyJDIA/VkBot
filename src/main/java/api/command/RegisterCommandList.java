package api.command;

import api.command.defaults.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegisterCommandList {
    private static final Set<Command> commands = new HashSet<>();//Классы уникальные
    static {
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
        registerCommand(new TimerCommand());
    }

    public static void registerCommand(Command command) {
        commands.add(command);
    }

    @Contract(pure = true)
    public static Collection<Command> getRegisteredCommands() {
        return commands;
    }
    public static void unregisterAll() {
        commands.clear();
    }
    public static void registerAll(@NotNull Iterable<? extends Command> commands) {
        commands.forEach(RegisterCommandList::registerCommand);
    }
}