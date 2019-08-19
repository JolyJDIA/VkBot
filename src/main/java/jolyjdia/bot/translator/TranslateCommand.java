package jolyjdia.bot.translator;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import jolyjdia.bot.translator.language.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Pattern;

public class TranslateCommand extends Command {
    private static final Pattern COMPILE = Pattern.compile("^[A-Za-z]+$");

    TranslateCommand() {
        super("translate",
                "<Текст>",
                "перевод текстов(Автоопределение языка)"
        );
        setAlias("перевод");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length >= 2) {
            Language lang = COMPILE.matcher(args[1]).matches() ? Language.RUSSIAN : Language.ENGLISH;
            @NonNls String translate = "язык " + lang.name() + "?\n" + "Перевод:\n";
            try {
                translate += YandexTraslate.translate(lang, StringBind.toString(args));
            } catch (IOException e) {
                ObedientBot.sendMessage("Ошибка", sender.getPeerId());
                return;
            }
            ObedientBot.sendMessage(translate, sender.getPeerId());
        } else {
            ObedientBot.sendMessage("Использование: " + getArguments(), sender.getPeerId());
        }
    }
}
