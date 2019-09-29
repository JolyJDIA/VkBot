package jolyjdia.bot.geo;

import api.Bot;
import api.command.defaults.HappyBirthdayBoatCommand;
import api.module.Module;
import com.maxmind.geoip2.DatabaseReader;
import jolyjdia.bot.Loader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public class GeoLoad implements Module {
    private boolean next = true;

    @Override
    public final void onLoad() {
        try {
            DatabaseReader reader = new DatabaseReader.Builder(new File(
                    Objects.requireNonNull(Loader.class.getClassLoader().getResource("GeoLite2-City.mmdb")).getFile())
            ).build();
            Bot.getBotManager().registerCommand(new GeoCommand(reader));
            /**
             * ИЗ-ЗА ТОГО, ЧТО Я ВЫРУБАЮ БОТА ЮЗАЮ ТАКОЕ ГОВНО
             */
            Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
                LocalDateTime from = LocalDateTime.now();

                if (next && from.getHour() == 20) {
                    next = false;
                    long days = DAYS.between(from.toLocalDate(), LocalDate.of(2019, 10, 12));
                    if (days == 0) {
                        for (int id : Bot.getProfileList().getChats()) {
                            Bot.sendMessage(HappyBirthdayBoatCommand.HAPPY, id);
                        }
                        Bot.editChat(HappyBirthdayBoatCommand.HAPPY, 2000000001);
                    }
                    String reply = String.format(HappyBirthdayBoatCommand.TO, days);
                    for (int id : Bot.getProfileList().getChats()) {
                        Bot.sendMessage(reply, id);
                    }
                    Bot.editChat(reply, 2000000001);
                } else if(LocalTime.now().getHour() > 20) {
                    next = true;
                }
            }, 1000, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
