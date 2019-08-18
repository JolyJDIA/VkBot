package jolyjdia.bot.geo;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import jolyjdia.bot.translator.YandexTraslate;
import jolyjdia.bot.translator.language.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GeoCommand extends Command {
/*
    private static final Pattern IPv4 =
            Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
*/

    private final DatabaseReader reader;
    GeoCommand(DatabaseReader reader) {
        super("/geo <IP>",
                "узнать местоположение по айпи");
        setAlias("geo");
        setPermission("roflanbot.geo", "У вас нет прав");
        this.reader = reader;

    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (noPermission(sender)) {
            return;
        }
        if(args.length == 2) {
            ObedientBot.sendMessage(getInfo(args[1]), sender.getPeerId());
        } else {
            ObedientBot.sendMessage(getUsageMessage(), sender.getPeerId());
        }
    }
    @NotNull
    private final String getInfo(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            CityResponse response = reader.city(address);
            Country country = response.getCountry();
            Location location = response.getLocation();
            String countryName = country.getName();
            String cityName = response.getCity().getName();
            @NonNls String info = "Страна: ";
            try {
                String name = YandexTraslate.translate(Language.RUSSIAN, countryName + ' ' + cityName);
                String[] array = name.split(" ");
                info += array[0];
                info += "\nГород: "+array[1];
            } catch (IOException e) {
                info += countryName;
                info += "\nГород: "+cityName;
            }
            info += "\nШирота: " + location.getLatitude() +
                    "\nдолгота: " + location.getLongitude();
            return info;
        } catch (UnknownHostException e) {
            return "Это не айпи 0_o";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        return "Некоторый данные не удалось загрузить(";
    }
}
