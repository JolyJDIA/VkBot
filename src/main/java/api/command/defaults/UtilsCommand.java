package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
import api.utils.VkUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.video.Video;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class UtilsCommand extends Command {

    public UtilsCommand() {
        super("utils", "команды-утилиты");
        setAlias("uptime", "calendar", "convert_ts", "current_ts", "search", "pattern");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        switch (args[0]) {
            case "utils" -> {
                if(args.length != 1) {
                    return;
                }
                sender.sendMessage(String.valueOf(getAlias()));
            }
            case "uptime" -> {
                if(args.length != 1) {
                    return;
                }
                RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
                long uptime = mxBean.getUptime();

                @NonNls String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(uptime),
                        TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime)),
                        TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime)));
                sender.sendMessage("Время работы " + hms);
            }
            case "calendar" -> {
                if(args.length != 1) {
                    return;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Время: HHч mmм ssс\nДата: dd.MM.yyyy");
                sender.sendMessage(formatter.format(LocalDateTime.now()));
            }
            case "convert_ts" -> {
                if(args.length != 2) {
                    return;
                }
                try {
                    long millis = Long.parseLong(args[1]);
                    LocalDateTime ofInstant = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
                    sender.sendMessage(String.valueOf(ofInstant));
                } catch (NumberFormatException e) {
                    sender.sendMessage("Это не число(long)");
                }
            }
            case "current_ts" -> {
                if(args.length != 1) {
                    return;
                }
                sender.sendMessage(String.valueOf(ZonedDateTime.now().toInstant().toEpochMilli()));
            }
            case "search" -> {
                if(args.length < 2) {
                    return;
                }
                StringBuilder builder = new StringBuilder();
                String title = StringBind.toString(args);
                try {
                    List<Video> videos = Bot.getVkApiClient().videos().search(VkUtils.USER_ACTOR, title)
                            .execute().getItems();
                    for (Video video : videos) {
                        builder.append("video").append(video.getOwnerId()).append('_').append(video.getId()).append(',');
                    }
                    builder.substring(0, builder.length() - 1);
                    sender.sendMessage(null, builder.toString());
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }
            }
            case "pattern" -> {
                if(args.length < 3) {
                    return;
                }
                Pattern regex = Pattern.compile(args[1]);
                sender.sendMessage(Arrays.toString(regex.split(StringBind.toString(2, args))));
            }
        }
    }
}
