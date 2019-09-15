package api;

import api.scheduler.BotScheduler;
import com.vk.api.sdk.objects.messages.Keyboard;

public interface RoflanBot {
    void sendMessage(String message, int peerId);
    void sendKeyboard(String message, int peerId, Keyboard keyboard);
    void editChat(String title, int peerId);
    BotScheduler getScheduler();
}
