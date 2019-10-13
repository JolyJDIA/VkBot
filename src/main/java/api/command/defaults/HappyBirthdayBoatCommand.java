package api.command.defaults;

import api.Bot;
import api.command.Command;
import api.storage.User;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class HappyBirthdayBoatCommand extends Command {
    public static final String HAPPY =
            "\uD83D\uDC9E\uD83C\uDF7A\uD83D\uDD25Happy Birthday RoflanBoat\uD83D\uDC9E\uD83C\uDF7A\uD83D\uDD25";

    public static final String TO = "\uD83D\uDD25ДР-ROFLANBOAT ЧЕРЕЗ : %s дней\uD83D\uDD25";

    public HappyBirthdayBoatCommand() {
        super("др");
    }

    @Override
    public final void execute(@NonNls User sender, @NotNull String[] args) {
        if(args.length == 1) {
            LocalDate from = LocalDate.now();
            LocalDate to = LocalDate.of(2019, 10, 12);
            long days = DAYS.between(from, to);
            if(days == 0) {
                Bot.editChat(HAPPY, 2000000001);
            }
            sender.sendMessageFromChat(String.format(TO, days));
        }
    }
}
