package jolyjdia.bot.activity;

import api.command.defaults.HappyCommand;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import api.utils.TimingsHandler;
import api.utils.VkUtils;
import api.utils.timeformat.TemporalDuration;
import api.utils.timeformat.TimeFormatter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
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
    private boolean offline;
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
            () -> "Осторожно!!! Злой динозаврик!",
            () -> "Быстродействие бота " + TimingsHandler.format(Bot.getScheduler().getTimingsHandler().getAverageTPS()[0])+"/20.0",
            () -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Время: HHч mmм ssс Дата: dd.MM.yyyy");
                return formatter.format(LocalDateTime.now());
            },
            () -> "java мастер",
            () -> "Советую автору пережить переходный возраст\uD83D\uDC4D\uD83C\uDFFB"
    );
    private int index;
    private static final Map<String, String> ACTIVITIES = ImmutableMap.<String, String>builder()
            .put("idea64.exe", "\uD83D\uDCBBCoding in IntelliJ IDEA(среда разработки)")
            .put("javaw.exe", "\uD83C\uDF0DMinecraft\uD83C\uDF0D")
            .put("Discord.exe", "✅Онлайн в Дискорде")
            .put("Telegram.exe", "✅Онлайн в Телеграме")
            .build();

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new ActivityCommand(this));
        Bot.getBotManager().registerEvent(this);
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                @NonNls String text = getOnlineFormat() + STATS.get(index).call();
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .text(text)
                        .execute();
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .groupId(Bot.getGroupId())
                        .text(text)
                        .execute();
                this.index = index == STATS.size()-1 ? 0 : ++index;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1800);
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().account().setOnline(VkUtils.USER_ACTOR).execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 6000);
    }

    @Override
    public final void onDisable() {
        try {
            Bot.getVkApiClient().status()
                    .set(VkUtils.USER_ACTOR)
                    .text("Осторожно! Злой динозаврик!")
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
    @EventLabel
    public static void onMsg(@NotNull NewMessageEvent e) {
        if(e.getUser().getUserId() == 482340506) {
            e.getUser().sendMessage(e.getMessage().getText());
        }
    }

    public final boolean isOffline() {
        return offline;
    }

    public final void setOffline(boolean offline) {
        this.offline = offline;
    }
    @NonNls
    @Contract(pure = true)
    public final @NotNull String getOnlineFormat() {
        return (this.offline ? "ОФФЛАЙН" : "ОНЛАЙН") + "\uD83D\uDC41\u200D\uD83D\uDDE8";
    }
}
