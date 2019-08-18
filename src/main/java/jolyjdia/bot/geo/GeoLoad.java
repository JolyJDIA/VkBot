package jolyjdia.bot.geo;

import api.JavaModule;
import api.command.RegisterCommandList;
import com.maxmind.geoip.LookupService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class GeoLoad extends JavaModule {
    @NonNls private static final String LINK_IP =
            "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz";
    @NonNls private static final String LINK_CITY =
            "http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz";
    @Override
    public final void onLoad() {
        try {
            LookupService serviceIp = new LookupService(updateOrDownload("GeoIP.dat", LINK_IP));
            LookupService serviceCity = new LookupService(updateOrDownload("GeoLiteCity.dat", LINK_CITY));
            RegisterCommandList.registerCommand(new GeoCommand(serviceIp, serviceCity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @NotNull
    private static File updateOrDownload(String res, String url) throws IOException {
        File file = new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources", res);
        if (file.exists()) {
            return file;
        }
        URL e = new URL(url);
        URLConnection conn = e.openConnection();
        conn.setConnectTimeout(10000);
        conn.connect();
        InputStream input = conn.getInputStream();
        if (url.endsWith(".gz")) {
            input = new GZIPInputStream(input);
        }
        try (FileOutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[2048];
            for (int length = input.read(buffer); length >= 0; length = input.read(buffer)) {
                output.write(buffer, 0, length);
            }
        }
        input.close();
        return file;
    }
}
