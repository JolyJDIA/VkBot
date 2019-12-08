package jolyjdia.bot.activity;

import api.command.defaults.HappyCommand;
import api.module.Module;
import api.utils.VkUtils;
import api.utils.timeformat.TemporalDuration;
import api.utils.timeformat.TimeFormatter;
import com.google.common.collect.ImmutableMap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class ActivityLoad implements Module {
    private static final Map<String, String> ACTIVITIES = ImmutableMap.<String, String>builder()
            .put("idea64.exe", "⛾Coding in IntelliJ IDEA(среда разработки)⛾")
            .put("Discord.exe", "✅Онлайн в Дискорде")
            .put("Telegram.exe", "✅Онлайн в Телеграме")
            .build();
    private boolean loop;

    @Override
    public final void onLoad() {
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .text(this.loop ?
                                String.format(HappyCommand.NEW_YEAR, getNewYearInStatus()) :
                                getApplications())
                        .execute();
                this.loop = !loop;
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .groupId(Bot.getGroupId())
                        .text(String.format(HappyCommand.NEW_YEAR, getNewYearInStatus()))
                        .execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 2000);
    }

    @NonNls
    public static @NotNull String getApplications() {
        return "Сейчас: " +
                ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(ProcessHandle.Info::command)
                .distinct()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Paths::get)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(ACTIVITIES::containsKey)
                .limit(2)
                .collect(StringBuilder::new,
                        (set, item) -> set.append(ACTIVITIES.get(item)).append(' '),
                        (set, item) -> set.substring(0, set.length() - 3));
    }
    public static @NotNull String getNewYearInStatus() {
        return TemporalDuration.of(1, 1, 0,0).toFormat(TimeFormatter.DAYS, TimeFormatter.HOURS, TimeFormatter.MINUTES);
    }
}
