package jolyjdia.bot.puzzle;

import api.Bot;
import api.command.Command;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import api.storage.User;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NotNull;

public class Puzzle implements Module, Listener {
    private Answer answer;
    private boolean math;
    private boolean next = true;

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
/*        ObedientBot.SCHEDULER.scheduleSyncRepeatingTask(() -> {
            if (next) {
                math = !math;
                answer = math ? new MathPuzzle() : new TextPuzzle();
                next = false;
            }
            ObedientBot.sendMessage("Развитие лодки!\n"+answer.getStringFormatAnswer(), 2000000001);
        }, 3000, 3000);*/
        Bot.getBotManager().registerCommand(new Puzzle.GeneratePuzzleCommand(this));
    }
    @EventLabel
    public final void onSend(@NotNull NewMessageEvent e) {
        if(next) {
            return;
        }
        Message msg = e.getMessage();
        if(msg.getText().equalsIgnoreCase(answer.getAnswer())) {
            this.next = true;
            e.getUser().sendMessageFromHisChat("Ура! Вы угадали ответ!");
        }
    }
    public static class GeneratePuzzleCommand extends Command {
        private final Puzzle puzzle;

        GeneratePuzzleCommand(Puzzle puzzle) {
            super("ask", "сгенерировать загадку");
            this.puzzle = puzzle;
        }

        @Override
        public final void execute(User sender, @NotNull String[] args) {
            if(args.length == 1) {
                if (puzzle.next) {
                    puzzle.math = !puzzle.math;
                    puzzle.answer = puzzle.math ? new MathPuzzle() : new TextPuzzle();
                    puzzle.next = false;
                }
                sender.sendMessageFromHisChat(puzzle.answer.getStringFormatAnswer());
            }
        }
    }
}
