package api;

import api.scheduler.BotScheduler;
import com.vk.api.sdk.objects.messages.Keyboard;

public final class Bot {
    private static RoflanBot roflanBot;

    public static void setBot(RoflanBot bot) {
        if (roflanBot != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Bot");
        }
        roflanBot = bot;
    }

    public static void sendMessage(String message, int peerId) {
        roflanBot.sendMessage(message, peerId);
    }

    public static void sendKeyboard(String message, int peerId, Keyboard keyboard) {
        roflanBot.sendKeyboard(message, peerId, keyboard);
    }

    public static void editChat(String title, int peerId) {
        roflanBot.editChat(title, peerId);
    }
    public static BotScheduler getScheduler() {
        return roflanBot.getScheduler();
    }
}
