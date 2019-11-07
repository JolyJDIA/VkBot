package jolyjdia.bot.utils;

import api.command.Command;
import api.storage.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Locale;

public class PasswordCommand extends Command {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    @NonNls private static final String STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

    PasswordCommand() {
        super("password", "[Длина пароля]", "[NEW] генерирует пароль");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            String password = SECURE_RANDOM.ints(40, 33, 122)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            sender.sendMessageFromChat(password);
        } else if(args.length == 2) {
            try {
                sender.sendMessageFromChat(generate(Integer.parseInt(args[1])));
            } catch (NumberFormatException e) {
                sender.sendMessageFromChat("Это не число");
            }
        }
    }

    private static @NotNull String generate(int length) {
        if (length <= 0) {
            return "";
        }
        if(length > 150) {
            return "Длина пароля слишком большая";
        }
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(STRING.length());
            String value = String.valueOf(STRING.charAt(index));
            if(MathUtils.RANDOM.nextBoolean()) {
                value = value.toUpperCase(Locale.ENGLISH);
            }
            password.append(value);
        }
        return password.toString();
    }
}
