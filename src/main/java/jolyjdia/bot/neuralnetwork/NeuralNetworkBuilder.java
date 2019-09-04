package jolyjdia.bot.neuralnetwork;

import jolyjdia.bot.neuralnetwork.nn.Matrix;
import jolyjdia.bot.neuralnetwork.nn.TrainerNeural;
import jolyjdia.bot.neuralnetwork.nn.datastructs.DataSet;
import jolyjdia.bot.neuralnetwork.nn.datastructs.TextData;
import jolyjdia.bot.neuralnetwork.nn.model.Model;
import jolyjdia.bot.neuralnetwork.nn.util.NetworkBuilder;
import jolyjdia.bot.neuralnetwork.word2vec.LearnDocVec;
import jolyjdia.bot.neuralnetwork.word2vec.Word2VEC;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class NeuralNetworkBuilder {
    private Word2VEC vec;
    private final Model model;

    public NeuralNetworkBuilder(LearnDocVec learnDocVec) {

        DataSet data = new TextData(learnDocVec);

        double learningRate = 0.001;
        double initParamsStdDev = 0.08;

        Random rng = new Random();
        this.model = NetworkBuilder.makeFeedForward(
                data.inputSize,
                20,
                1,
                20,
                data.getModelOutputUnitToUse(),
                data.getModelOutputUnitToUse(),
                initParamsStdDev,
                rng);

        int reportEveryNthEpoch = 10;
        new TrainerNeural.BuilderTrainer()
                .setTrainingEpoch(1000)
                .setLearningRate(learningRate)
                .setModel(model)
                .setDataSet(data)
                .setReportEveryNthEpoch(reportEveryNthEpoch)
                .setRandom(rng)
                .setMinLoss(0.4).build();

    }
    @Contract(pure = true)
    public static double distance(@NotNull double[] vectorA, double[] vectorB) {
        double dist = 0;
        for (int i = 0; i < vectorA.length; ++i) {
            double sub = vectorA[i] - vectorB[i];
            dist += sub * sub;
        }
        return Math.sqrt(dist);
    }
    public final String answerNeural(String msg) {
        String text = "";
        double k = 0;
        Matrix input = new Matrix(NeuralNetrwork.getDocVec().getDocVec(msg));
        double[] d = TextData.generateText(input, model);
        System.out.println(Arrays.toString(d));
        for (Map.Entry<String, double[]> output : NeuralNetrwork.getDocVec().getDocVector().entrySet()) {
            double sim = distance(d, output.getValue());
            if (sim > k) {
                k = sim;
                text = output.getKey();
            }
        }
        return text;
    }
}
