package jolyjdia.bot.generateText;

import api.JavaModule;
import api.command.RegisterCommandList;
import jolyjdia.bot.generateText.neuralnetwork.NeuralNetworkHelper;
import jolyjdia.bot.generateText.neuralnetwork.TrainerNeural;
import jolyjdia.bot.generateText.neuralnetwork.datasets.TextGenerationUnbroken;
import jolyjdia.bot.generateText.neuralnetwork.model.Model;

public class GeneratorLoad extends JavaModule {
    public static TextGenerationUnbroken data;
    @Override
    public final void onLoad() {
        int totalSequences = 2000;
        int sequenceMinLength = 10;
        int sequenceMaxLength = 100;
        data = new TextGenerationUnbroken(
                "D:\\IdeaProjects\\VkBot\\src\\main\\resources\\PaulGraham.txt",
                totalSequences, sequenceMinLength, sequenceMaxLength);

        int bottleneckSize = 10;
        int hiddenDimension = 25;
        int hiddenLayers = 1;
        double learningRate = 0.001;
        double initParamsStdDev = 0.08;

        Model lstm = NeuralNetworkHelper.makeLstmWithInputBottleneck(
                data.getInputDimension(),
                bottleneckSize,
                hiddenDimension,
                hiddenLayers,
                data.getOutputDimension(),
                data.getModelOutputUnitToUse(),
                initParamsStdDev);

        int reportEveryNthEpoch = 10;
        int trainingEpochs = 250;

        new TrainerNeural.BuilderTrainer()
                .setTrainingEpoch(trainingEpochs)
                .setLearningRate(learningRate)
                .setInitFromSaved(true)
                .setOverwriteSaved(false)
                .setModel(lstm)
                .setDataSet(data)
                .setFromPath("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\PaulGraham.ser")
                .setReportEveryNthEpoch(reportEveryNthEpoch)
                .setMinLoss(0.4)
                .build();

        RegisterCommandList.registerCommand(new GeneratorCommand());
    }
}
