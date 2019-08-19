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
    }

    public static void registerCommand(Command listener) {
        commands.add(listener);
    }

    @Contract(" -> new")
    @NotNull
    public static Set<Command> getRegisteredCommands() {
        return commands;
    }
    public static void unregisterAll() {
        commands.clear();
    }
    public static void registerAll(@NotNull Collection<? extends Command> commands) {
        commands.forEach(RegisterCommandList::registerCommand);
    }
}