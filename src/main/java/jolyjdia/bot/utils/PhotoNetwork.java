package jolyjdia.bot.utils;

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

public class PhotoNetwork {
    private final Model model;

    private static int strPos(int g) {//РЕНДЕР ПИКСЕЛЕЙ(типо пикселей xD)
        return g >= 250 ? 0 : 1;
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
                array[i] = strPos(value);
                ++i;
            }
        }
        return array;
    }

    public PhotoNetwork() throws Exception {
        double[][] input = new double[2][10000];
        double[][] output = new double[2][10000];

        input[0] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВХОД1.png");
        input[1] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВХОД2.png");
       // input[2] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВХОД3.png");

        output[0] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВЫХОД1.png");
        output[1] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВЫХОД2.png");
      //  output[2] = photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВЫХОД3.png");

        DemoData data = new DemoData(input, output);
        model = NeuralNetworkHelper.makeLstm(
                data.inputDimension,
                200,
                1,
                data.outputDimension,
                data.getModelOutputUnitToUse(),
                0.08);

        new TrainerNeural.BuilderTrainer()
                .setTrainingEpoch(99999)
                .setLearningRate(0.001)
                .setModel(model)
                .setDataSet(data)
                .setMinLoss(1.1)
                .build();
    }

    public final Model getModel() {
        return model;
    }
    public final double[] answerVector(double[] msg) throws Exception {
        Matrix input1 = new Matrix(msg);
        return TestData.generateText(input1, model);
    }
}
