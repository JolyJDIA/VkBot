package jolyjdia.bot.activity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jolyjdia.api.command.defaults.HappyCommand;
import jolyjdia.api.event.Listener;
import jolyjdia.api.module.Module;
import jolyjdia.api.utils.TimingsHandler;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.api.utils.timeformat.TemporalDuration;
import jolyjdia.api.utils.timeformat.TimeFormatter;
import jolyjdia.bot.Bot;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

public class ActivityLoad implements Module, Listener {
    public static final List<Callable<String>> STATS = ImmutableList.of(
            () -> """
                    Сейчас напиши статус типа дипрессия хачу умиреть
                    Страницу закрою типа загадочность
                    В сахраненках будут мальчики и цветы!!!
                    ~14 лет😎👍🏻😈""",
            () -> """
                    Если ты видишь мои слезы
                    знай
                    ВИНОВАТА АМЕРИКА😎👍🏻😈""",
            ActivityLoad::getActivityProcesses,
            () -> String.format(HappyCommand.NEW_YEAR, TemporalDuration.of(1, 1, 0,0).toFormat(
                    TimeFormatter.DAYS,
                    TimeFormatter.HOURS,
                    TimeFormatter.MINUTES)
            ),
            () -> "Быстродействие бота " + TimingsHandler.format(Bot.getScheduler().getTimingsHandler().getAverageTPS()[0])+"/20.0",
            () -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Время: HHч mmм ssс Дата: dd.MM.yyyy");
                return formatter.format(LocalDateTime.now());
            },
            () -> "java-мастер",
            () -> "Советую автору пережить переходный возраст\uD83D\uDC4D\uD83C\uDFFB"
    );
    private int index;
    private static final Map<String, String> ACTIVITIES = ImmutableMap.<String, String>builder()
          //  .put("idea64.exe", "\uD83D\uDCBBCoding in IntelliJ IDEA(среда разработки)")
            .put("javaw.exe", "Играет в \uD83C\uDF0DMinecraft▦")
            .put("Discord.exe", "\uD83D\uDE08Онлайн в Дискорде")
            .put("Telegram.exe", "✈Онлайн в Телеграме")
            .build();

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
        Bot.getBotManager().registerCommand(new LikesCommand());
        Bot.getBotManager().registerCommand(new CommentCommand());
        Bot.getScheduler().runRepeatingSyncTaskAfter(() -> {
            try {
                Bot.getVkApiClient().status()
                        .set(VkUtils.ZAVR)
                        .groupId(Bot.getGroupId())
                        .text(STATS.get(index).call())
                        .execute();
                this.index = index == STATS.size()-1 ? 0 : ++index;
                Bot.getVkApiClient().status()
                        .set(VkUtils.ZAVR)
                        .text(getActivityProcesses())
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 0, 1800);
        Bot.getScheduler().runRepeatingSyncTaskAfter(() -> {
            try {
                Bot.getVkApiClient().account().setOnline(VkUtils.ZAVR).execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 6000);
        //1 тик - 50 миллисекунд
        //20 тиков в 1 секунде
        //20 * 3600
    }

    @Override
    public final void onDisable() {
        try {
            Bot.getVkApiClient().status()
                    .set(VkUtils.ZAVR)
                    .text("Осторожно!!! Злой динозаврик!!!")//Наш Мир - костыль
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    @NonNls
    public static @NotNull String getActivityProcesses() {
        StringBuilder builder = ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(ProcessHandle.Info::command)
                .distinct()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Paths::get)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(ACTIVITIES::containsKey)
                .limit(3)
                .collect(StringBuilder::new,
                        (set, item) -> set.append(ACTIVITIES.get(item)).append(", "),
                        StringBuilder::append);
        return "Сейчас: " + builder.substring(0, builder.length()-2);
    }
}
