package jolyjdia.bot.generateText.neuralnetwork.datasets;

import api.utils.MathUtils;
import api.utils.ObedientBot;
import jolyjdia.bot.generateText.neuralnetwork.autodiff.Graph;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.DataSequence;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.DataSet;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.DataStep;
import jolyjdia.bot.generateText.neuralnetwork.datastructs.Matrix;
import jolyjdia.bot.generateText.neuralnetwork.loss.LossSoftmax;
import jolyjdia.bot.generateText.neuralnetwork.model.LinearUnit;
import jolyjdia.bot.generateText.neuralnetwork.model.Model;
import jolyjdia.bot.generateText.neuralnetwork.model.Nonlinearity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class TextGenerationUnbroken extends DataSet {
    private static final long serialVersionUID = 3974448212113540917L;
    private static final int REPORT_SEQUENCE_LENGTH = 100;
    private final Map<Integer, String> indexToChar = new HashMap<>();
    private int dimension;

    @NotNull
    public final String generateText(@NotNull Model model, int steps, double temperature) {
        model.resetState();
        Graph g = new Graph(false);
        Matrix input = new Matrix(dimension);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < steps; ++i) {
            Matrix logprobs = model.forward(input, g);
            Matrix probs = LossSoftmax.getSoftmaxProbs(logprobs, temperature);

            int indxChosen = MathUtils.pickIndexFromRandomVector(probs);
            result.append(indexToChar.get(indxChosen));
            Arrays.fill(input.w, 0);
            input.w[indxChosen] = 1.0;
        }
        return result.toString().replace("\n", "\"\n\t\"");
    }

    public TextGenerationUnbroken(String path, int totalSequences, int sequenceMinLength, int sequenceMaxLength) {
        File file = new File(path);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(lines == null) {
            return;
        }
        StringBuilder text = new StringBuilder();
        lines.forEach(line -> text.append(line).append('\n'));

        Set<String> chars = new HashSet<>();
        int id = 0;
        Map<String, Integer> charToIndex = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            if (!chars.contains(ch)) {
                chars.add(ch);
                charToIndex.put(ch, id);
                indexToChar.put(id, ch);
                id++;
            }
        }
        dimension = chars.size();
        List<DataSequence> sequences = new ArrayList<>();
        for (int s = 0; s < totalSequences; s++) {
            List<double[]> vecs = new ArrayList<>();
            int len = ObedientBot.RANDOM.nextInt(sequenceMaxLength - sequenceMinLength + 1) + sequenceMinLength;
            int start = ObedientBot.RANDOM.nextInt(text.length() - len);
            for (int i = 0; i < len; i++) {
                String ch = String.valueOf(text.charAt(i + start));
                double[] vec = new double[dimension];
                vec[charToIndex.get(ch)] = 1.0;
                vecs.add(vec);
            }
            DataSequence sequence = new DataSequence();
            for (int i = 0; i < vecs.size() - 1; i++) {
                sequence.steps.add(new DataStep(vecs.get(i), vecs.get(i+1)));
            }
            sequences.add(sequence);
        }
        System.out.println("Уникальных символов: "+dimension);

        setTraining(sequences);
        setLossTraining(new LossSoftmax());
        setLossReporting(new LossSoftmax());
        setInputDimension(sequences.get(0).steps.get(0).input.w.length);
        int loc = 0;
        while (sequences.get(0).steps.get(loc).targetOutput == null) {
            loc++;
        }
        setOutputDimension(sequences.get(0).steps.get(loc).targetOutput.w.length);
    }
    @NotNull
    public String getOutput(Model model) {
        double[] temperatures = {0.85, 0.8, 0.75, 0.5, 0.45};//НАДА ПОФИКСИТЬ
        StringBuilder builder = new StringBuilder();
        for (double temperature : temperatures) {
            String guess = generateText(model, REPORT_SEQUENCE_LENGTH, temperature);
            builder.append(guess).append('\n');
        }
        return builder.toString();
    }
    @Override
    public final void displayReport(Model model) {
        System.out.println("========================================");
        double[] temperatures = {1, 0.75, 0.5, 0.25, 0.1};
        for (double temperature : temperatures) {
            System.out.println("\nTemperature "+temperature+" prediction:");
            String guess = generateText(model, REPORT_SEQUENCE_LENGTH, temperature);
            System.out.println("\t\"..." + guess + "...\"");
        }
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    @Override
    public final Nonlinearity getModelOutputUnitToUse() {
        return new LinearUnit();
    }
}
