package jolyjdia.bot.neuralnetwork;

import jolyjdia.bot.neuralnetwork.nn.Matrix;
import jolyjdia.bot.neuralnetwork.nn.TrainerNeural;
import jolyjdia.bot.neuralnetwork.nn.datastructs.DataSet;
import jolyjdia.bot.neuralnetwork.nn.datastructs.TextData;
import jolyjdia.bot.neuralnetwork.nn.model.Model;
import jolyjdia.bot.neuralnetwork.nn.util.NetworkBuilder;
import jolyjdia.bot.neuralnetwork.word2vec.LearnDocVec;
import jolyjdia.bot.neuralnetwork.word2vec.Word2VEC;

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
        this.model = NetworkBuilder.makeLstm(
                data.inputSize,
                20,
                1,
                20,
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
                .setMinLoss(0.04).build();

    }
    public static double distance(double[] a, double[] b) {
        double dotProduct = 0;
        double normASum = 0;
        double normBSum = 0;

        for (int i = 0; i < a.length; ++i) {
            dotProduct += a[i] * b[i];
            normASum += a[i] * a[i];
            normBSum += b[i] * b[i];
        }

        double eucledianDist = Math.sqrt(normASum) * Math.sqrt(normBSum);
        return dotProduct / eucledianDist;
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
