package jolyjdia.bot.puzzle;

import jolyjdia.api.command.Command;
import jolyjdia.api.event.EventLabel;
import jolyjdia.api.event.Listener;
import jolyjdia.api.event.messages.NewMessageEvent;
import jolyjdia.api.module.Module;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.MathUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import vk.objects.messages.Message;

public class Puzzle implements Module, Listener {
    private int answer;
    @NonNls private String format;
    private boolean next = true;

    @Override
    public final void onLoad() {
        /**Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            if (next) {
                this.generate();
            }
            Bot.sendMessage("Развитие лодки!\n"+getStringFormatAnswer(), 2000000001);
        }, 15000, 15000);*/
        Bot.getBotManager().registerCommand(new Puzzle.GeneratePuzzleCommand(this));
        Bot.getBotManager().registerEvent(this);
    }
    public final void generate() {
        boolean token = MathUtils.RANDOM.nextBoolean();
        int first = MathUtils.RANDOM.nextInt(1000);
        int second = MathUtils.RANDOM.nextInt(1000);

        this.answer = token ? first + second : first - second;
        this.format = first + (token ? " + " : " - ") + second;
        this.next = false;
    }
    @EventLabel
    public final void onSend(@NotNull NewMessageEvent e) {
        if(this.next) {
            return;
        }
        Message msg = e.getMessage();
        if(msg.getText().equalsIgnoreCase(String.valueOf(answer))) {
            this.next = true;
            e.getChat().sendMessage("Ура! Вы угадали ответ!");
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
                    puzzle.generate();
                }
                sender.getChat().sendMessage(getStringFormatAnswer());
            }
        }
        private @NotNull String getStringFormatAnswer() {
            return "Решите пример: "+puzzle.format;
        }
    }
}
