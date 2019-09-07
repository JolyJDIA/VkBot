package jolyjdia.bot.generateText.neuralnetwork;

import api.utils.FileSerializer;
import jolyjdia.bot.generateText.neuralnetwork.autodiff.Graph;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.DataSequence;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.DataSet;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.DataStep;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.Matrix;
import jolyjdia.bot.generateText.neuralnetwork.loss.Loss;
import jolyjdia.bot.generateText.neuralnetwork.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrainerNeural {
    private static final double SMOOTH_EPSILON = 1e-8;
    private static final double REGULARIZATION = 0.000001; // L2 сила регуляризации
    private static final double DECAYRATE = 0.999;
    private static final double GRADIENE_CLIP_VALUE = 5;
    public static Model glModel;

    @NotNull
    public static String process(@NotNull StringBuilder builder, int epoch, int all) {
        builder.append('[');
        int d = epoch * 60 / all;
        for (int f = 0; f < 60; ++f) {
            builder.append(f < d ? "-" : "·");
        }
        builder.append(']');
        return builder.toString();
    }

    @NotNull
    public static String processConsole(@NotNull StringBuilder builder, double loss, double minloss) {
        builder.append('[');
        double d = minloss * 60 / loss;
        for (int f = 0; f < 60; ++f) {
            builder.append(f < d ? "=" : "·");
        }
        builder.append(']');
        return builder.toString();
    }

    public static class BuilderTrainer {
        @NonNls
        private int trainingEpochs;
        private double learningRate;
        private Model model;
        private DataSet data;
        private int reportEveryNthEpoch;
        private boolean initFromSaved;
        private boolean overwriteSaved;
        private String savePath;
        private double minloss;

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setTrainingEpoch(int i) {
            this.trainingEpochs = i;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setLearningRate(double i) {
            this.learningRate = i;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setModel(Model model) {
            this.model = model;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setDataSet(DataSet data) {
            this.data = data;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setReportEveryNthEpoch(int i) {
            this.reportEveryNthEpoch = i;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setInitFromSaved(boolean b) {
            this.initFromSaved = b;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setOverwriteSaved(boolean b) {
            this.overwriteSaved = b;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setFromPath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        @Contract("_ -> this")
        public final TrainerNeural.BuilderTrainer setMinLoss(double i) {
            this.minloss = i;
            return this;
        }

        public final void build() {
            System.out.println("--------------------------------------------------------------");
            if (initFromSaved) {
                System.out.println("инициализация из сохраненного состояния...");
                glModel = (Model) FileSerializer.deserialize(savePath);
                return;
            }
            new Thread(() -> {
                for (int i = 0; i < trainingEpochs; ++i) {
                    double reportedLossTrain = pass();
                    if (Double.isNaN(reportedLossTrain) || Double.isInfinite(reportedLossTrain)) {
                        return;
                    }
                    String info = "epoch["+(i+1)+'/'+trainingEpochs+']'+"\tloss = "+String.format("%.5f", reportedLossTrain);
                    System.out.println(info);

                    if (i % reportEveryNthEpoch == reportEveryNthEpoch - 1) {
                        data.displayReport(model);
                    }
                }
                if (overwriteSaved) {
                    System.out.println("Сохраняю данные...");
                    FileSerializer.serialize(savePath, model);
                    System.out.println("Данные сохранены");
                }
            }).start();
        }
        final double pass() {
            List<DataSequence> sequences = data.getTraining();
            Loss lossTraining = data.getLossTraining();
            Loss lossReporting = data.getLossReporting();
            double numerLoss = 0;
            double denomLoss = 0;
            for (DataSequence seq : sequences) {
                model.resetState();
                Graph g = new Graph(true);
                for (DataStep step : seq.steps) {
                    Matrix output = model.forward(step.input, g);
                    if (step.targetOutput == null) {
                        continue;
                    }
                    double loss = lossReporting.measure(output, step.targetOutput);
                    if (Double.isNaN(loss) || Double.isInfinite(loss)) {
                        return loss;
                    }
                    numerLoss += loss;
                    denomLoss++;
                    lossTraining.backward(output, step.targetOutput);
                }
                g.backward(); //backprop dw values
                updateModelParams(learningRate); //update params
            }
            return numerLoss/denomLoss;
        }
        public final void updateModelParams(double stepSize) {
            model.getParameters().forEach(m -> {
                for (int i = 0; i < m.w.length; i++) {
                    double mdwi = m.dw[i];
                    m.stepCache[i] = m.stepCache[i] * DECAYRATE + (1 - DECAYRATE) * mdwi * mdwi;
                    if (mdwi > GRADIENE_CLIP_VALUE) {
                        mdwi = GRADIENE_CLIP_VALUE;
                    }
                    if (mdwi < -GRADIENE_CLIP_VALUE) {
                        mdwi = -GRADIENE_CLIP_VALUE;
                    }
                    m.w[i] += - stepSize * mdwi / Math.sqrt(m.stepCache[i] + SMOOTH_EPSILON) - REGULARIZATION * m.w[i];
                    m.dw[i] = 0;
                }
            });
        }
    }
}
