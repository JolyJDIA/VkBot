package jolyjdia.bot.utils;

import api.command.Command;
import api.storage.User;
import jolyjdia.bot.Bot;
import jolyjdia.bot.utils.nn.NeuralNetworkHelper;
import jolyjdia.bot.utils.nn.TextGeneration;
import jolyjdia.bot.utils.nn.TrainerNeural;
import jolyjdia.bot.utils.nn.datastructs.DataSet;
import jolyjdia.bot.utils.nn.model.Model;
import org.jetbrains.annotations.NotNull;

public class NeuralCommand extends Command {
    private static Model lstm;
    static {
        train();
    }
    NeuralCommand() {
        super("speak");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            try {
                sender.sendMessageFromChat(TextGeneration.generateText(lstm, 200, 0.5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void train() {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            try {
                DataSet data = new TextGeneration("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\brycov.txt");

                lstm = NeuralNetworkHelper.makeRnn(
                        data.inputDimension,
                        200,
                        1,
                        data.outputDimension,
                        data.getModelOutputUnitToUse(),
                        data.getModelOutputUnitToUse(),
                        0.08);

                new TrainerNeural.BuilderTrainer()
                        .setDataSet(data)
                        .setLearningRate(0.0001)
                        .setTrainingEpoch(9999)
                        .setMinLoss(0.1)
                        .setModel(lstm)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
