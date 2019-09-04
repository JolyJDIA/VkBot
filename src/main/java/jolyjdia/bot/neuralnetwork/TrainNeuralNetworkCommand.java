package jolyjdia.bot.neuralnetwork;

import api.command.Command;
import api.entity.User;
import api.utils.ObedientBot;
import api.utils.StringBind;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class TrainNeuralNetworkCommand extends Command {
    private static final Pattern COMPILE = Pattern.compile(" : ");
    private final NeuralNetrwork main;
    public TrainNeuralNetworkCommand(NeuralNetrwork main) {
        super("train");
        this.main = main;
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length > 2) {
            String[] text = COMPILE.split(StringBind.toString(args));
            NeuralNetrwork.getDocVec().learnString(text[0]);
            NeuralNetrwork.getDocVec().learnString(text[1]);
            main.setNetworkBuilder(new NeuralNetworkBuilder(NeuralNetrwork.getDocVec()));
        //    NeuralNetrwork.getDocVec().saveModelY("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\doc");
            ObedientBot.sendMessage("Вроде понял", sender.getPeerId());
        }
    }
}
