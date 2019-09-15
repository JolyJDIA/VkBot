package jolyjdia.bot.raid;

import api.Bot;
import api.command.Command;
import api.entity.User;
import api.scheduler.RoflanRunnable;
import api.utils.KeyboardUtils;

public class RaidCommand extends Command {
    protected RaidCommand() {
        super("raid");
    }

    /*
    0101
    1010
    0101
    1010
     */

    @Override
    public final void execute(User sender, String[] args) {
        if(args.length == 1) {
         //   ObedientBot.sendKeyboard("ЛЯ ЧЕРТ", sender.getPeerId(), new Keyboard().setButtons(RaidKeyboard.BOARD));
            new RoflanRunnable() {
                int i;
                @Override
                public void run() {
                    sender.sendMessageFromHisChat("ДАДАДА");
                    ++i;
                    if(i == 5) {
                        cancel();
                    }
                }
            }.runTaskTimer(0, 50);
        } else if(args.length == 2) {
            if(args[1].equalsIgnoreCase("stop")) {
                Bot.sendKeyboard("close", sender.getPeerId(), KeyboardUtils.EMPTY_KEYBOARD);
            }
        }
    }
}
