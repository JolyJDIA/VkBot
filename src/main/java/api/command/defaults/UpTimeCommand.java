package api.command.defaults;

import api.command.Command;
import api.entity.User;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

public class UpTimeCommand extends Command {
    public UpTimeCommand() {
        super("uptime", "время с момента включения бота");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
            long uptime = mxBean.getUptime();

            @NonNls String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(uptime),
                    TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime)),
                    TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime)));
            sender.sendMessageFromHisChat("Время работы " + hms);
        }
    }
}