package jolyjdia.bot.geo;

import api.JavaModule;
import api.command.RegisterCommandList;
import com.maxmind.geoip2.DatabaseReader;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;

public class GeoLoad extends JavaModule {
    /**@NonNls private static final String LINK_COUNTRY =
            "https://geolite.maxmind.com/download/geoip/database/GeoLite2-Country.tar.gz";
    @NonNls private static final String LINK_CITY =
            "https://geolite.maxmind.com/download/geoip/database/GeoLite2-City.tar.gz";*/

    @NonNls
    private static final String PATH =
            "D:\\IdeaProjects\\VkBot\\src\\main\\resources\\GeoLite2-City.mmdb";

    @Override
    public final void onLoad() {
        try {
            DatabaseReader reader = new DatabaseReader.Builder(new File(PATH)).build();
            RegisterCommandList.registerCommand(new GeoCommand(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
