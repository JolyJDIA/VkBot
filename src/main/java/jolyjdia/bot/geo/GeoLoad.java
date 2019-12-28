package jolyjdia.bot.geo;

import api.module.Module;
import com.maxmind.geoip2.DatabaseReader;
import jolyjdia.bot.Bot;
import jolyjdia.bot.Loader;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GeoLoad implements Module {

    @Override
    public final void onLoad() {
        try {
            DatabaseReader reader = new DatabaseReader.Builder(new File(
                    Objects.requireNonNull(Loader.class.getClassLoader().getResource("GeoLite2-City.mmdb")).getFile())
            ).build();
            Bot.getBotManager().registerCommand(new GeoCommand(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
