package jolyjdia.bot.neuralnetwork;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import org.jetbrains.annotations.NotNull;

public class TestNeuralNetworkCommand extends Command {
    private final NeuralNetrwork main;
    public TestNeuralNetworkCommand(NeuralNetrwork main) {
        super("test");
        this.main = main;
    }

    @Override
    public void execute(User sender, @NotNull String[] args) {
        if(args.length > 2) {
            String text = StringBind.toString(args);
            ObedientBot.sendMessage(main.getNetworkBuilder().answerNeural(text), sender.getPeerId());
        }
    }
}
