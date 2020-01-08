package jolyjdia.bot.utils;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.Chat;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class RollCommand extends Command {
    RollCommand() {
        super("roll", "<Мин> <Макс>", "случайное число в заданном диапазоне");
    }

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        Chat<?> chat = sender.getChat();
        if(args.length == 1) {
            chat.sendMessage(String.valueOf(MathUtils.RANDOM.nextInt(100) + 1));
        } else if(args.length >= 2 && args.length < 4) {
            try {
                int start = Integer.parseInt(args[1]);
                if (args.length == 2) {
                    chat.sendMessage(String.valueOf(MathUtils.random(start)));
                } else {
                    int end = Integer.parseInt(args[2]);
                    if(end < start) {
                        chat.sendMessage("Максимальное число должно быть больше минимального");
                        return;
                    }
                    chat.sendMessage(String.valueOf(MathUtils.random(start, end)));
                }
            } catch (NumberFormatException e) {
                chat.sendMessage("Это не число!");
            }
        }
    }
}
