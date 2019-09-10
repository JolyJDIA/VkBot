package jolyjdia.bot.translator;

import api.JavaModule;
import api.command.RegisterCommandList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@NonNls
public class YandexTraslate extends JavaModule {
    @NonNls private static final String KEY =
            "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20190604T115104Z.df1684c5248f0d39.b57d76995665926fb0fb0e8e6c3a3de2e36aa00e";
    @Override
    public final void onLoad() {
        RegisterCommandList.registerCommand(new TranslateCommand());
    }
    @Nullable private static URL url;
    static {
        try {
            url = new URL(KEY);
        } catch (MalformedURLException e) {
            url = null;
        }
    }
    @NotNull public static String translate(@NotNull @NonNls Language lang, String input) throws IOException {
        @NonNls StringBuilder builder = new StringBuilder();
        if(url == null) {
            return "URL Error";
        }
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())) {
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, StandardCharsets.UTF_8) + "&lang=" + lang);
            try (InputStream response = connection.getInputStream(); ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = response.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                String json = result.toString(StandardCharsets.UTF_8);
                builder.append(json, json.indexOf('[') + 2, json.indexOf(']') - 1);

            }
        }
        return builder.toString();
    }
}