package api;

import api.module.ModuleLoader;
import api.scheduler.BotScheduler;
import api.storage.ProfileList;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Keyboard;

import java.util.Properties;

public interface RoflanBot {
    void sendMessage(String message, int peerId);
    void sendKeyboard(String message, int peerId, Keyboard keyboard);
    void editChat(String title, int peerId);
    ProfileList getProfileList();
    BotManager getBotManager();
    BotScheduler getScheduler();
    int getGroupId();
    String getAccessToken();
    Properties getConfig();
    GroupActor getGroupActor();
    ModuleLoader getModuleLoader();
}
