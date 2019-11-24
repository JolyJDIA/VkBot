package jolyjdia.bot.utils;

import api.utils.VkUtils;
import api.utils.chat.MessageChannel;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import jolyjdia.bot.Bot;
import jolyjdia.bot.utils.nn.NeuralNetworkHelper;
import jolyjdia.bot.utils.nn.TestData;
import jolyjdia.bot.utils.nn.TextGeneration;
import jolyjdia.bot.utils.nn.TrainerNeural;
import jolyjdia.bot.utils.nn.datastructs.DataSet;
import jolyjdia.bot.utils.nn.matrix.Matrix;
import jolyjdia.bot.utils.nn.model.Model;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PhotoTool {
    private Model model;
    private final PhotoNetwork photoNetwork;

    public PhotoTool() {
        this.photoNetwork = new PhotoNetwork();
    }
    final void getNeuralReply(double[] input, int peerId) {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = img.createGraphics();

        try {
            double[] output = photoNetwork.answerVector(input);
            int i = 0;
            for(int y = 0; y < 100; ++y) {
                for(int x = 0; x < 100; ++x) {
                    if(output[i] > 0.75) {
                        g2d.drawLine(x,y, x, y);
                    }
                    ++i;
                }
            }
            ImageIO.write(img, "PNG", new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\test.png"));
            sendPhoto(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\test.png"), peerId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g2d.dispose();
        }

    }
    public static void sendPhoto(File photo, int peerId) {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpPost httppost = new HttpPost(String.valueOf(Bot.getVkApiClient().photos().getMessagesUploadServer(Bot.getGroupActor())
                    .peerId(peerId)
                    .execute()
                    .getUploadUrl()));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("photo",  new FileBody(photo))
                    .build();
            httppost.setEntity(reqEntity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);

            JsonElement jelement = new JsonParser().parse(responseBody);

            List<Photo> photos = Bot.getVkApiClient().photos()
                    .saveMessagesPhoto(Bot.getGroupActor(), jelement.getAsJsonObject().get("photo").getAsString())
                    .server(jelement.getAsJsonObject().get("server").getAsInt())
                    .hash(jelement.getAsJsonObject().get("hash").getAsString()).execute();
            MessageChannel.sendAttachments(VkUtils.attachment(photos.get(0)), peerId);
        } catch (ClientException | ApiException | IOException e ) {
            e.printStackTrace();
        }
    }


    public final double[] answerVector(double[] input) throws Exception {
        return TestData.generateText(new Matrix(input), model);
    }
    private void train() {
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
