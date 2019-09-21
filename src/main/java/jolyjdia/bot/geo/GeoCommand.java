package jolyjdia.bot.geo;

import api.Bot;
import api.command.Command;
import api.storage.User;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import jolyjdia.bot.translator.Language;
import jolyjdia.bot.translator.YandexTraslate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoCommand extends Command {
    private final DatabaseReader reader;
    private static final Pattern IPV4 =
            Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
    GeoCommand(DatabaseReader reader) {
        super("geo", "<IP-адрес>",
                "узнать местоположение по айпи");
        setPermission("roflanbot.geo", "У вас нет прав");
        this.reader = reader;

    }
    @Override
    public final void execute(User sender, String[] args) {
        Bot.getScheduler().runTask(() -> {
            if (noPermission(sender)) {
                return;
            }
            if(args.length == 2) {
                Matcher matcher = IPV4.matcher(args[1]);
                if (!matcher.matches()) {
                    sender.sendMessageFromHisChat("Это не айпи 0_o");
                    return;
                }
                sender.sendMessageFromHisChat(getInfo(args[1]));
            } else {
                sender.sendMessageFromHisChat(getArguments());
            }
        });
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
                String name = YandexTraslate.translate(Language.RUSSIAN, countryName + ':' + cityName);
                String[] array = name.split(":");
                info += array[0];
                info += "\nГород: "+array[1];
            } catch (IOException e) {
                info += countryName;
                info += "\nГород: "+cityName;
            }
            info += "\nШирота: " + location.getLatitude() +
                    "\nдолгота: " + location.getLongitude();
            return info;
        } catch (UnknownHostException | GeoIp2Exception e) {
            return "Некоторый данные не удалось загрузить(";
        } catch (IOException e) {
            return "IOException";
        }
    }
}
