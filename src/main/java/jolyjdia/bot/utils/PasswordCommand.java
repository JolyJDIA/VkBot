package jolyjdia.bot.utils;

import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

public class PasswordCommand extends Command {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    PasswordCommand() {
        super("password", "[Длина пароля]", "[NEW] генерирует пароль");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            String password = SECURE_RANDOM.ints(20, 48, 122)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            sender.sendMessageFromChat(password);
        } else if(args.length == 2) {
            int size;
            try {
                size = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessageFromChat("Это не число");
                return;
            }
            if(size > 50) {
                sender.sendMessageFromChat("Длина пароля ограничена 50 символами");
                return;
            }
            String password = SECURE_RANDOM.ints(size, 48, 122)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            sender.sendMessageFromChat(password);
        }
    }
}