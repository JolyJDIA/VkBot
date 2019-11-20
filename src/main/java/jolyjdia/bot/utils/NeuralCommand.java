package jolyjdia.bot.utils;

import api.command.Command;
import api.storage.User;
import jolyjdia.bot.Bot;
import jolyjdia.bot.utils.nn.NeuralNetworkHelper;
import jolyjdia.bot.utils.nn.TestData;
import jolyjdia.bot.utils.nn.TextGeneration;
import jolyjdia.bot.utils.nn.TrainerNeural;
import jolyjdia.bot.utils.nn.datastructs.DataSet;
import jolyjdia.bot.utils.nn.matrix.Matrix;
import jolyjdia.bot.utils.nn.model.Model;
import org.jetbrains.annotations.NotNull;

public class NeuralCommand extends Command {
    private static Model model;
    private PhotoNetwork photoNetwork;
    NeuralCommand() {
        super("speak");
        /**try {
            photoNetwork = new PhotoNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = img.createGraphics();

        try {
            double[] input = PhotoNetwork.photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ТЕСТ.png");
            double[] output = photoNetwork.answerVector(input);
            int i = 0;
            for(int y = 0; y < 100; ++y) {
                for(int x = 0; x < 100; ++x) {
                    if(output[i] > 0.95) {
                        g2d.drawLine(x,y, x, y);
                    }
                    ++i;
                }
            }
            ImageIO.write(img, "PNG", new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\test.png"));
            System.out.println("Изображение готово");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g2d.dispose();
        }*/
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
        if(args.length == 1) {
            try {
                //sender.sendMessageFromChat(TextGeneration.generateText(model, 100, 0.3));
                double[] input = PhotoNetwork.photo("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\)1.png");
                double[] output = photoNetwork.answerVector(input);
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < output.length; ++i) {
                    if(output[i] > 0.025) {
                        builder.append("#####");
                    } else {
                        builder.append("     ");
                    }
                    if(i != 0 && i % 6 == 0) {
                        builder.append('\n');
                    }
                }
                System.out.println(builder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static final double[] answerVector(double[] msg) throws Exception {
        Matrix input = new Matrix(msg);
        return TestData.generateText(input, model);
    }
    private static void train() {
        Bot.getScheduler().runTaskAsynchronously(() -> {
            try {
                DataSet data = new TextGeneration("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\word2vec.txt");

                model = NeuralNetworkHelper.makeFeedForward(
                        data.inputDimension,
                        200,
                        1,
                        data.outputDimension,
                        data.getModelOutputUnitToUse(),
                        data.getModelOutputUnitToUse(),
                        0.08);

                new TrainerNeural.BuilderTrainer()
                        .setDataSet(data)
                        .setLearningRate(0.008)
                        .setTrainingEpoch(99999)
                        .setMinLoss(1.0)
                        .setModel(model)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
