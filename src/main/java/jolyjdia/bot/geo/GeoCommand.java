package jolyjdia.bot.geo;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import com.maxmind.geoip.Country;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoCommand extends Command {
    private static final Pattern IPv4 =
            Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
    private final LookupService serviceIp;
    private final LookupService serviceCity;

    GeoCommand(LookupService serviceIp, LookupService serviceCity) {
        super("/geo <IP>",
                "узнать местоположение по айпи");
        setAlias("geo");
        setPermission("roflanbot.geo", "У вас нет прав");
        this.serviceIp = serviceIp;
        this.serviceCity = serviceCity;
    }
    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if (noPermission(sender)) {
            return;
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
    private final String getInfo(@NonNls String host) {
        @NonNls String text = "Информация по IP: " + host+ '\n';
        try {
            Country country = serviceIp.getCountry(host);
            Location ip = serviceCity.getLocation(host);
            text += "Страна: " + country.getCode() + ", " + country.getName() + '\n';
            text += "Город: " + ip.city + '\n';
            text += "Широта: " + ip.latitude+ '\n';
            text += "Долгота: " + ip.longitude;
        } catch (RuntimeException e) {
            text += "\nНекоторый данные не удалось загрузить(";
        }
        return text;
    }
}
