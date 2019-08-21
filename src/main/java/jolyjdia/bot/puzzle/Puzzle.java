package jolyjdia.bot.puzzle;

import api.JavaModule;
import api.command.Command;
import api.command.RegisterCommandList;
import api.entity.User;
import api.event.EventHandler;
import api.event.Listener;
import api.event.RegisterListEvent;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Puzzle extends JavaModule implements Listener {
    static final Random RANDOM = new Random();
    private Answer answer;
    private boolean math;
    private boolean next = true;

    @Override
    public final void onLoad() {
/*        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (next) {
                    math = !math;
                    answer = math ? new MathPuzzle() : new TextPuzzle();
                    next = false;
                }
                System.out.println("d");
                //\n" + answer.getStringFormatAnswer() + '\n'
                ObedientBot.sendMessage("Чат-игра!", 2000000020);
            }
        }, 1000, 1000);//60*1000*/
        RegisterCommandList.registerCommand(new Puzzle.GeneratePuzzleCommand(this));
        RegisterListEvent.registerEvent(this);
    }
    @EventHandler
    public final void onSend(@NotNull NewMessageEvent e) {
        if(next) {
            return;
        }
        Message msg = e.getMessage();
        if(msg.getText().equalsIgnoreCase(answer.getAnswer())) {
            this.next = true;
            ObedientBot.sendMessage("Ура! Вы угадали ответ!", msg.getPeerId());
        }
    }
    public static class GeneratePuzzleCommand extends Command {
        private final Puzzle puzzle;

        GeneratePuzzleCommand(Puzzle puzzle) {
            super("ask", "сгенерировать загадку");
            this.puzzle = puzzle;
        }


        @Override
        public final void execute(User sender, String[] args) {
            if (puzzle.next) {
                puzzle.math = !puzzle.math;
                puzzle.answer = puzzle.math ? new MathPuzzle() : new TextPuzzle();
                puzzle.next = false;
            }
            ObedientBot.sendMessage(puzzle.answer.getStringFormatAnswer(), 2000000020);
        }
    }
}
