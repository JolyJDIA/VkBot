package jolyjdia.bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Loader {

    public static void main(String[] args) throws ClientException, ApiException {
        //test(Bot.getVkApiClient().photos().getMessagesUploadServer(Bot.getGroupActor()).peerId(2000000002).execute().getUploadUrl());
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Bot.getScheduler().mainThreadHeartbeat();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(Bot.getVkApiClient(), Bot.getGroupActor());
        try {
            handler.run();
        } catch (ApiException | ClientException | RuntimeException e) {
            System.out.println("ТЕХНИЧЕСКИЕ ШОКОЛАДКИ");
            handler.run();
        }
    }
    public static void test(URL url) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(String.valueOf(url));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("photo", new FileBody(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\ВХОД1.png")))
                    .build();

            httpPost.setEntity(reqEntity);

            httpclient.execute(httpPost);
            System.out.println(readAll(httpPost.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String readAll(InputStream rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
