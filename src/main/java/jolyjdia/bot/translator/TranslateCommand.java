package jolyjdia.bot.translator;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Pattern;

public class TranslateCommand extends Command {
    private static final Pattern COMPILE = Pattern.compile("[A-Za-z]+$");

    TranslateCommand() {
        super("translate", "<текст>", "перевод текста");
        setAlias("перевод");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            if (args.length >= 2) {
                String text = StringBind.toString(args);
                Language lang = isEnglish(text) ? Language.RUSSIAN : Language.ENGLISH;
                String translate;
                try {
                    translate = YandexTraslate.translate(lang, StringBind.toString(args));
                } catch (IOException e) {
                    sender.getChat().sendMessage("Ошибка");
                    return;
                }
                sender.getChat().sendMessage(translate);
            } else {
                sender.getChat().sendMessage("Использование: " + getUseCommand());
            }
        });
    }
    public static boolean isEnglish(@NotNull String text) {
        for(int i = 0; i < text.length(); ++i) {
            String c = String.valueOf(text.charAt(i));
            if(!COMPILE.matcher(c).matches()) {
                continue;
            }
            return true;
        }
        return false;
    }
}
