package jolyjdia.bot;

import api.BotManager;
import api.RoflanBot;
import api.scheduler.BotScheduler;
import api.storage.ProfileList;
import api.utils.MathUtils;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.LongPollSettings;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ObedientBot implements RoflanBot {
    private final BotScheduler scheduler = new BotScheduler();
    private final ProfileList profileList = new ProfileList(new File(
            Objects.requireNonNull(Loader.class.getClassLoader().getResource("users.json")).getFile()
    ));
    private final BotManager registerListEvent = new BotManager();
    private final Properties properties = new Properties();
    private final String accessToken;
    private final int groupId;
    private final GroupActor groupActor;

    public ObedientBot() throws ClientException, ApiException {
        try (InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.groupId = Integer.parseInt(properties.getProperty("groupId"));
        this.accessToken = properties.getProperty("accessToken");
        this.groupActor = new GroupActor(groupId, accessToken);
        LongPollSettings settings = Loader.getVkApiClient().groups().getLongPollSettings(groupActor, groupId).execute();
        if (settings == null) {
            return;
        }
        if (settings.getIsEnabled()) {
            return;
        }
        Loader.getVkApiClient().groups()
                .setLongPollSettings(groupActor, groupId)
                .enabled(true)
                .wallPostNew(true)
                .messageNew(true)
                .audioNew(true)
                .execute();
    }

    @Contract(pure = true)
    @Override
    public GroupActor getGroupActor() {
        return groupActor;
    }

    @Contract(pure = true)
    @Override
    public int getGroupId() {
        return groupId;
    }

    @Contract(pure = true)
    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Contract(pure = true)
    @Override
    public Properties getConfig() {
        return properties;
    }

    @Contract(pure = true)
    @Override
    public ProfileList getProfileList() {
        return profileList;
    }

    @Contract(pure = true)
    @Override
    public BotManager getBotManager() {
        return registerListEvent;
    }

    @Contract(pure = true)
    @Override
    public BotScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void sendMessage(String msg, int peerId) {
        scheduler.runTask(() -> {
            try {
                send().peerId(peerId).message(msg).execute();
            } catch (ApiException | ClientException ignored) {}
        });
    }

    @Override
    public void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        scheduler.runTask(() -> {
            try {
                send().peerId(peerId).keyboard(keyboard).message(msg).execute();
            } catch (ApiException | ClientException ignored) {}
        });
    }

    @Override
    public void editChat(String title, int peerId) {
        scheduler.runTask(() -> {
            try {
                Loader.getVkApiClient().messages().editChat(groupActor, peerId - 2000000000, title).execute();
            } catch (ApiException | ClientException ignored) {}
        });
    }
    private MessagesSendQuery send() {
        return Loader.getVkApiClient().messages()
                .send(groupActor)
                .randomId(MathUtils.RANDOM.nextInt(10000))
                .groupId(groupId);
    }
}
