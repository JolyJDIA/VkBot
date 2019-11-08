package jolyjdia.bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public class Loader {

    public static void main(String[] args) throws ClientException, ApiException {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Bot.getScheduler().mainThreadHeartbeat();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(Bot.getVkApiClient(), Bot.getGroupActor());
        try {
            handler.run();
        } catch (ApiException | ClientException e) {
            handler.run();
        }
    }
}
