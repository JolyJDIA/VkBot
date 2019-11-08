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

            /**SLocalDateTime from = LocalDateTime.now();
            long days = DAYS.between(from.toLocalDate(), LocalDate.of(from.getYear(), 10, 12));
            if (days == 0) {
                for (int id : Bot.getProfileList().getChats()) {
                    Bot.sendMessage(HappyBirthdayBoatCommand.HAPPY, id);
                }
                Bot.editChat(HappyBirthdayBoatCommand.HAPPY, 2000000001);
            }
            tring reply = String.format(HappyBirthdayBoatCommand.TO, days);
            Bot.sendMessage(reply, 2000000001);
            Bot.sendMessage(reply, 2000000018);
            Bot.sendMessage(reply, 2000000013);

            Bot.editChat(reply, 2000000001);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
