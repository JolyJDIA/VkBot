package jolyjdia.bot.generateText;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import jolyjdia.bot.generateText.neuralnetwork.TrainerNeural;
import jolyjdia.bot.generateText.neuralnetwork.datasets.TextGenerationUnbroken;
import org.jetbrains.annotations.NotNull;

public class GeneratorCommand extends Command {
    GeneratorCommand() {
        super("generator", "сгенерировать текст песни");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            ObedientBot.sendMessage(TextGenerationUnbroken.getOutput(TrainerNeural.glModel), sender.getPeerId());
        }
    }
}
