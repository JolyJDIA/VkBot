package jolyjdia.bot.translator;

import api.JavaModule;
import api.command.RegisterCommandList;
import jolyjdia.bot.translator.language.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@NonNls
public class YandexTraslate extends JavaModule {
    @NonNls private static final String KEY =
            "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20190604T115104Z.df1684c5248f0d39.b57d76995665926fb0fb0e8e6c3a3de2e36aa00e";
    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new TranslateCommand());
    }
    @NotNull
    public static String translate(@NotNull @NonNls Language lang, String input) throws IOException {
        @NonNls StringBuilder builder = new StringBuilder();
        URL url = new URL(KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())) {
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, StandardCharsets.UTF_8) + "&lang=" + lang);
            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String json = scanner.nextLine();
                builder.append(json, json.indexOf('[') + 2, json.indexOf(']') - 1);
            }
            response.close();
        }
        return builder.toString();
    }
}