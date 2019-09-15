package jolyjdia.bot.geo;

import api.command.RegisterCommandList;
import api.utils.JavaModule;
import com.maxmind.geoip2.DatabaseReader;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;

public class GeoLoad extends JavaModule {
    @NonNls private static final String PATH =
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
