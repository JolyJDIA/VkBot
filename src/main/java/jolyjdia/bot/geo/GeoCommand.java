package jolyjdia.bot.geo;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoCommand extends Command {
    private static final Pattern IPv4 =
            Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");

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
          //  return;
        }
        if(args.length == 2) {
            Matcher matcher = IPv4.matcher(args[1]);
            if(!matcher.matches()) {
                ObedientBot.sendMessage("Это не айпи 0_o", sender.getPeerId());
                return;
            }
            ObedientBot.sendMessage(getInfo(args[1]), sender.getPeerId());
        } else {
            ObedientBot.sendMessage(getUsageMessage(), sender.getPeerId());
        }
    }
    @NotNull
    public final String getInfo(String host) {
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(host);
            CityResponse response = reader.city(ipAddress);
            Country country = response.getCountry();
            Location location = response.getLocation();
            @NonNls String info = "Страна: "+ country.getName()+ ", "+country.getIsoCode()+'\n';
            info += "Город: "+ response.getCity().getName()+ '\n';
            info += "Широта: "+ location.getLatitude()+ '\n';
            info += "долгота: "+ location.getLongitude();
            return info;
        } catch (UnknownHostException | GeoIp2Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Некоторый данные не удалось загрузить(";
    }
}
