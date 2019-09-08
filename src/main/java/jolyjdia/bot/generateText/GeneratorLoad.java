package jolyjdia.bot.generateText;

import api.JavaModule;
import api.command.RegisterCommandList;
import jolyjdia.bot.generateText.neuralnetwork.NeuralNetworkHelper;
import jolyjdia.bot.generateText.neuralnetwork.TrainerNeural;
import jolyjdia.bot.generateText.neuralnetwork.datasets.TextGenerationUnbroken;
import jolyjdia.bot.generateText.neuralnetwork.model.Model;

public class GeneratorLoad extends JavaModule {
    private TextGenerationUnbroken data;
    @Override
    public final void onLoad() {
        int totalSequences = 2000;
        int sequenceMinLength = 10;
        int sequenceMaxLength = 100;
        data = new TextGenerationUnbroken(
                "D:\\IdeaProjects\\VkBot\\src\\main\\resources\\PaulGraham.txt",
                totalSequences, sequenceMinLength, sequenceMaxLength);
        new TrainerNeural.BuilderTrainer()
                .setInitFromSavedAndSave(true)
                .setFromPath("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\PaulGraham.ser")
/*                .setTrainingEpoch(500)
                .setLearningRate(0.001)
                .setModel(getModel())
                .setDataSet(data)
                .setReportEveryNthEpoch(10)
                .setMinLoss(0.1)*/
                .build();

        RegisterCommandList.registerCommand(new GeneratorCommand(this));
    }
    private Model getModel() {
        int bottleneckSize = 10;
        int hiddenDimension = 25;
        int hiddenLayers = 1;
        double initParamsStdDev = 0.08;

        return NeuralNetworkHelper.makeLstmWithInputBottleneck(
                data.getInputDimension(),
                bottleneckSize,
                hiddenDimension,
                hiddenLayers,
                data.getOutputDimension(),
                data.getModelOutputUnitToUse(),
                initParamsStdDev);
    }

    public TextGenerationUnbroken getData() {
        return data;
    }
}
