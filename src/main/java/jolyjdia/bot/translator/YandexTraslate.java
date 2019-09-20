package jolyjdia.bot.translator;

import api.Bot;
import api.utils.JavaModule;
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

@NonNls public class YandexTraslate extends JavaModule {
    @NonNls private static final String KEY =
            "https://translate.yandex.net/api/v1.5/tr.json/translate?key="+ Bot.getConfig().getProperty("translateKey");
    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new TranslateCommand());
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
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") + "&lang=" + lang);//StandardCharsets.UTF_8
            try (InputStream response = connection.getInputStream(); ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = response.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                String json = result.toString("UTF-8");//StandardCharsets.UTF_8
                builder.append(json, json.indexOf('[') + 2, json.indexOf(']') - 1);

            }
        }
        return builder.toString();
    }
}