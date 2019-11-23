package jolyjdia.bot.utils;

import jolyjdia.bot.Bot;
import jolyjdia.bot.utils.nn.DemoData;
import jolyjdia.bot.utils.nn.NeuralNetworkHelper;
import jolyjdia.bot.utils.nn.TestData;
import jolyjdia.bot.utils.nn.TrainerNeural;
import jolyjdia.bot.utils.nn.matrix.Matrix;
import jolyjdia.bot.utils.nn.model.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PhotoNetwork {
    private Model model;

    public static double[] photo(BufferedImage image) {
        double[] array = new double[10000];
        if (image == null) {
            return array;
        }
        int i = 0;
        for (int y = 0; y < 100; ++y) {
            for (int x = 0; x < 100; ++x) {
                int rgb = 0xff000000 | image.getRGB(x, y);
                int value = (int) ((((rgb >> 16) & 0xFF) * 0.2989) + ((rgb & 0xFF) * 0.5870) + (((rgb >> 8) & 0xFF) * 0.1140));
               // array[i] = value == 0 ? 0 : 1.0/(255/value);
                array[i] = value >= 250 ? 0 : 1;
                ++i;
            }
        }
        return array;
    }

    public static double[] photo(String s) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[] array = new double[10000];
        if (image == null) {
            return array;
        }
        int i = 0;
        for (int y = 0; y < 100; ++y) {
            for (int x = 0; x < 100; ++x) {
                int rgb = 0xff000000 | image.getRGB(x, y);
                int value = (int) ((((rgb >> 16) & 0xFF) * 0.2989) + ((rgb & 0xFF) * 0.5870) + (((rgb >> 8) & 0xFF) * 0.1140));
               // array[i] = value == 0 ? 0 : 1.0/(255/value);
                array[i] = value >= 250 ? 0 : 1;
                ++i;
            }
        }
        return array;
    }

    public PhotoNetwork() {
        try {
            File pathDir = new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\train");
            double[][] input = new double[Objects.requireNonNull(pathDir.list()).length][10000];
            double[][] output = new double[Objects.requireNonNull(pathDir.list()).length][10000];
            String[] pathsFilesAndDir = pathDir.list();
            assert pathsFilesAndDir != null;
            int i = 0;
            for(String path : pathsFilesAndDir) {
                String s = path.substring(0, path.length()-4);
                if(s.charAt(1) != 'Ы') {
                    int index = Integer.parseInt(s.substring(4));
                    input[index] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\train\\" + path);
                    output[index] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\train\\ВЫХОД"+((i % 2 == 0) ? "1" : "2")+".png");
                    ++i;
                }
            }
            DemoData data = new DemoData(input, output);
            model = NeuralNetworkHelper.makeFeedForward(
                    data.inputDimension,
                    500,
                    1,
                    data.outputDimension,
                    data.getModelOutputUnitToUse(),
                    data.getModelOutputUnitToUse(),
                    0.08);
            Bot.getScheduler().runTaskAsynchronously(() -> {
                try {
                    new TrainerNeural.BuilderTrainer()
                            .setTrainingEpoch(99999)
                            .setLearningRate(0.001)
                            .setModel(model)
                            .setDataSet(data)
                            .setMinLoss(0.05)
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public final Model getModel() {
        return model;
    }
    public final double[] answerVector(double[] msg) throws Exception {
        Matrix input1 = new Matrix(msg);
        return TestData.generateText(input1, model);
    }
}
