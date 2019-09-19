package jolyjdia.bot.password;

import api.command.Command;
import api.entity.User;
import api.utils.MathUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PasswordCommand extends Command {
    @NonNls public static final String STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    PasswordCommand() {
        super("password", "[Длина пароля]", "[NEW] генерирует пароль");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            sender.sendMessageFromHisChat(generate(40));
        } else if(args.length == 2) {
            try {
                sender.sendMessageFromHisChat(generate(Integer.parseInt(args[1])));
            } catch (NumberFormatException e) {
                sender.sendMessageFromHisChat("Это не число");
            }
        }
    }

    @NotNull
    private static String generate(int length) {
        if (length <= 0) {
            return "";
        }
        if(length > 150) {
            return "Длина пароля слишком большая";
        }
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = MathUtils.RANDOM.nextInt(STRING.length());
            String value = String.valueOf(STRING.charAt(index));
            if(MathUtils.RANDOM.nextBoolean()) {
                value = value.toUpperCase(Locale.ENGLISH);
            }
            password.append(value);
        }
        return password.toString();
    }
}
