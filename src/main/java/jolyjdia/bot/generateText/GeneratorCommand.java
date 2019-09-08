package jolyjdia.bot.generateText;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import jolyjdia.bot.generateText.neuralnetwork.TrainerNeural;
import org.jetbrains.annotations.NotNull;

public class GeneratorCommand extends Command {
    private final GeneratorLoad load;
    GeneratorCommand(GeneratorLoad load) {
        super("generator", "сгенерировать текст песни");
        this.load = load;
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            ObedientBot.sendMessage(load.getData().getOutput(TrainerNeural.glModel), sender.getPeerId());
        }
    }
}
