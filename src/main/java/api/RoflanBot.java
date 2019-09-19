package api;

import api.command.RegisterCommandList;
import api.event.RegisterListEvent;
import api.file.ProfileList;
import api.scheduler.BotScheduler;
import com.vk.api.sdk.objects.messages.Keyboard;

import java.util.Properties;

public interface RoflanBot {
    void sendMessage(String message, int peerId);
    void sendKeyboard(String message, int peerId, Keyboard keyboard);
    void editChat(String title, int peerId);
    ProfileList getProfileList();
    RegisterCommandList getRegisterCommandList();
    RegisterListEvent getRegisterListEvent();
    BotScheduler getScheduler();
    int getGroupId();
    String getAccessToken();
    Properties getConfig();
}
