package jolyjdia.bot.utils.nn;

import jolyjdia.bot.utils.nn.autodiff.Graph;
import jolyjdia.bot.utils.nn.datastructs.DataSequence;
import jolyjdia.bot.utils.nn.datastructs.DataSet;
import jolyjdia.bot.utils.nn.datastructs.DataStep;
import jolyjdia.bot.utils.nn.matrix.Matrix;
import jolyjdia.bot.utils.nn.model.LossSoftmax;
import jolyjdia.bot.utils.nn.model.Model;
import jolyjdia.bot.utils.nn.model.Nonlinearity;
import jolyjdia.bot.utils.nn.model.SigmoidUnit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoData extends DataSet {
    private final List<DataSequence> list = new ArrayList<>();
    private final DataSequence step = new DataSequence();
    private double[][] input = new double[2][625];
    private double[][] output = new double[2][5];

    public DemoData(double[][] input, double[][] output) {
        this.input = input;
        this.output = output;
        List<DataSequence> sequences = new ArrayList<>();
        DataSequence sequence = new DataSequence();
        for (int i = 0; i < input.length; ++i) {
            sequence.steps.add(new DataStep(input[i], output[i]));
            sequences.add(sequence);
        }

        training = sequences;
        lossTraining = new LossSoftmax();
        lossReporting = new LossSoftmax();
        inputDimension = sequences.get(0).steps.get(0).input.w.length;
        int loc = 0;
        while (sequences.get(0).steps.get(loc).targetOutput == null) {
            loc++;
        }
        outputDimension = sequences.get(0).steps.get(loc).targetOutput.w.length;
    }

    public static double[] generateText(Matrix input, Model model) throws Exception {
        model.resetState();
        Graph g = new Graph(false);
        Matrix logprobs = model.forward(input, g);
        //   Matrix probs = LossSoftmax.getSoftmaxProbs(logprobs, temperature);
        return logprobs.w;
    }


    public final void init() {
        list.add(step);
        training = list;
    }
    private int i;

    @Override
    public final void sendReport(@NotNull Model model) throws Exception {

        model.resetState();
        Graph g = new Graph(false);
        Matrix logprobs = model.forward(new Matrix(input[i]), g);
        System.out.println("input: "+ Arrays.toString(input[i]));
        System.out.println("output: "+ Arrays.toString(output[i]));
        System.out.println("target: "+Arrays.toString(logprobs.w));
        ++i;
        if(i == 2) {
            i = 0;
        }
    }

    @Override
    public final @NotNull Nonlinearity getModelOutputUnitToUse() {
        return new SigmoidUnit();
    }
}
