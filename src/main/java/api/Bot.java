package api;

import api.command.HelpAllCommands;
import api.module.ModuleLoader;
import api.scheduler.BotScheduler;
import api.storage.ProfileList;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Keyboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;

import java.util.Properties;

@NonNls public final class Bot {
    private static RoflanBot roflanBot;

    public static void setBot(RoflanBot bot) {
        if (roflanBot != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Bot");
        }
        roflanBot = bot;
    }
    public static HelpAllCommands getHelpCommand() {
        return roflanBot.getHelpCommand();
    }
    public static int getGroupId() {
        return roflanBot.getGroupId();
    }

    @Contract(pure = true)
    public static String getAccessToken() {
        return roflanBot.getAccessToken();
    }

    public static BotManager getBotManager() {
        return roflanBot.getBotManager();
    }

    public static BotScheduler getScheduler() {
        return roflanBot.getScheduler();
    }

    public static ProfileList getProfileList() {
        return roflanBot.getProfileList();
    }

    public static Properties getConfig() {
        return roflanBot.getConfig();
    }

    public static void sendMessage(String message, int peerId) {
        roflanBot.sendMessage(message, peerId);
    }

    public static void sendKeyboard(String message, int peerId, Keyboard keyboard) {
        roflanBot.sendKeyboard(message, peerId, keyboard);
    }
    public static GroupActor getGroupActor() {
        return roflanBot.getGroupActor();
    }

    public static void editChat(String title, int peerId) {
        roflanBot.editChat(title, peerId);
    }

    public static ModuleLoader getModuleLoader() {
        return roflanBot.getModuleLoader();
    }
}
