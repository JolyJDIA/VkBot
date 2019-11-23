package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.TemporalDuration;
import api.utils.VkUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class HappyCommand extends Command {
    private static final String NEW_YEAR = "\uD83D\uDD25\uD83E\uDD76Новый Год через: %s\uD83E\uDD76\uD83D\uDD25";
    public HappyCommand() {
        super("др");
        setAlias("нг");
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .text(String.format(NEW_YEAR, getNewYearInStatus()))
                        .execute();
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .groupId(Bot.getGroupId())
                        .text(String.format(NEW_YEAR, getNewYearInStatus()))
                        .execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 2000);
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().account().setOnline(VkUtils.USER_ACTOR).voip(false).execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 6000);
    }
    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("др")) {
                String date = TemporalDuration.of(10, 12, 0,0).toString();
                sender.sendMessageFromChat("\uD83D\uDD25ДР-ROFLANBOAT\uD83D\uDD25 через: "+date + "\uD83D\uDD25");
            } else if(args[0].equalsIgnoreCase("нг")) {
                sender.sendMessageFromChat(String.format(NEW_YEAR, TemporalDuration.of(1, 1, 0,0).toString()));
            }
        }
    }

    public static String getNewYearInStatus() {
        return TemporalDuration.of(1, 1, 0,0)
                .toFormat(TemporalDuration.TimeFormatter.DAYS,
                        TemporalDuration.TimeFormatter.HOURS,
                        TemporalDuration.TimeFormatter.MINUTES);
    }
}