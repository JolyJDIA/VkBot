package jolyjdia.bot.geo;

import api.Bot;
import api.utils.JavaModule;
import com.maxmind.geoip2.DatabaseReader;
import jolyjdia.bot.Loader;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;

public class GeoLoad extends JavaModule {
    @NonNls private static final String PATH =
            Loader.class.getClassLoader().getResource("GeoLite2-City.mmdb").getFile();

    @Override
    public final void onLoad() {
        try {
            DatabaseReader reader = new DatabaseReader.Builder(new File(PATH)).build();
            Bot.getBotManager().registerCommand(new GeoCommand(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
