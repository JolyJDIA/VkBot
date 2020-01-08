package jolyjdia.api.command.defaults;

import jolyjdia.api.command.Command;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import vk.exceptions.ApiException;
import vk.exceptions.ClientException;
import vk.objects.video.Video;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UtilsCommand extends Command {

    public UtilsCommand() {
        super("utils", "команды-утилиты");
        setAlias("uptime", "calendar", "convert_ts", "current_ts", "search", "tasks");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        switch (args[0]) {
            case "utils" -> {
                if(args.length != 1) {
                    return;
                }
                sender.getChat().sendMessage(String.valueOf(getAlias()));
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
                sender.getChat().sendMessage("Время работы " + hms);
            }
            case "calendar" -> {
                if(args.length != 1) {
                    return;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Время: HHч mmм ssс\nДата: dd.MM.yyyy");
                sender.getChat().sendMessage(formatter.format(LocalDateTime.now()));
            }
            case "convert_ts" -> {
                if(args.length != 2) {
                    return;
                }
                try {
                    long millis = Long.parseLong(args[1]);
                    LocalDateTime ofInstant = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
                    sender.getChat().sendMessage(String.valueOf(ofInstant));
                } catch (NumberFormatException e) {
                    sender.getChat().sendMessage("Это не число(long)");
                }
            }
            case "current_ts" -> {
                if(args.length != 1) {
                    return;
                }
                sender.getChat().sendMessage(String.valueOf(ZonedDateTime.now().toInstant().toEpochMilli()));
            }
            case "search" -> {
                if(args.length < 2) {
                    return;
                }
                StringBuilder builder = new StringBuilder();
                String title = StringBind.toString(args);
                try {
                    List<Video> videos = Bot.getVkApiClient().videos().search(VkUtils.ZAVR, title)
                            .execute().getItems();
                    for (Video video : videos) {
                        builder.append("video").append(video.getOwnerId()).append('_').append(video.getId()).append(',');
                    }
                    builder.substring(0, builder.length() - 1);
                    sender.getChat().sendAttachments(builder.toString());
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }
            }
            case "tasks" -> {
                if(args.length != 1) {
                    return;
                }
                sender.getChat().sendMessage(
                        "Всего задач: "+Bot.getScheduler().taskCount()+
                             "\nПотоков: "+Runtime.getRuntime().availableProcessors());
            }
        }
    }
}
