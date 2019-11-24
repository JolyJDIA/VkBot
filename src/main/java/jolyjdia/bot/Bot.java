package jolyjdia.bot;

import api.command.HelpAllCommands;
import api.module.Module;
import api.module.ModuleLoader;
import api.permission.PermissionManager;
import api.scheduler.BotScheduler;
import api.storage.MySQL;
import api.storage.ProfileList;
import api.storage.UserBackend;
import api.utils.MathUtils;
import com.vk.api.sdk.actions.Groups;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import jolyjdia.bot.calculate.CalculatorRegister;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.newcalculator.InitCalc;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.smile.SmileLoad;
import jolyjdia.bot.translator.YandexTraslate;
import jolyjdia.bot.utils.UtilsModule;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public final class Bot {
    private static final VkApiClient vkApiClient = new VkApiClient(new HttpTransportClient());
    private static final BotScheduler scheduler = new BotScheduler();
    private static final BotManager manager = new BotManager();
    private static final Properties properties = new Properties();
    private static final ModuleLoader moduleLoader = new ModuleLoader();
    private static final HelpAllCommands helpCommand = new HelpAllCommands();
    private static final UserBackend userBackend;
    private static final int groupId;
    private static final String accessToken;
    private static final GroupActor groupActor;

    static {
        try (InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupId = Integer.parseInt(properties.getProperty("groupId"));
        accessToken = properties.getProperty("accessToken");
        groupActor = new GroupActor(groupId, accessToken);

        PermissionManager.registerPermissionGroups();
        userBackend = properties.getProperty("mysql").equalsIgnoreCase("true") ?
                new MySQL(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("url")) :
                new ProfileList(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));

        Groups groups = vkApiClient.groups();
        try {
            if (!groups.getLongPollSettings(groupActor, groupActor.getGroupId()).execute().getIsEnabled()) {
                groups.setLongPollSettings(groupActor, groupActor.getGroupId())
                        .enabled(true).apiVersion("5.101").wallPostNew(true).messageNew(true)
                        .audioNew(true).groupJoin(true).groupLeave(true).messageReply(true)
                        .messageEdit(true).messageAllow(true).messageDeny(true).messageTypingState(true)
                        .execute();
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        registerModules();
        loadModule();
    }
    private static void registerModules() {
        moduleLoader.registerModule(new CalculatorRegister());
        moduleLoader.registerModule(new YandexTraslate());
        moduleLoader.registerModule(new GeoLoad());
        moduleLoader.registerModule(new Puzzle());
        moduleLoader.registerModule(new UtilsModule());
        moduleLoader.registerModule(new SmileLoad());
        moduleLoader.registerModule(new InitCalc());
    }
    private static void loadModule() {
        moduleLoader.getModules().forEach(Module::onLoad);
        helpCommand.initializeHelp();
    }

    @Contract(pure = true)
    public static HelpAllCommands getHelpCommand() {
        return helpCommand;
    }

    @Contract(pure = true)
    public static ModuleLoader getModuleLoader() {
        return moduleLoader;
    }

    @Contract(pure = true)
    public static GroupActor getGroupActor() {
        return groupActor;
    }

    @Contract(pure = true)
    public static int getGroupId() {
        return groupId;
    }

    @Contract(pure = true)
    public static String getAccessToken() {
        return accessToken;
    }

    @Contract(pure = true)
    public static Properties getConfig() {
        return properties;
    }

    @Contract(pure = true)
    public static UserBackend getUserBackend() {
        return userBackend;
    }

    @Contract(pure = true)
    public static BotManager getBotManager() {
        return manager;
    }

    @Contract(pure = true)
    public static BotScheduler getScheduler() {
        return scheduler;
    }

    @Contract(pure = true)
    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }

    private static final Pattern COMPILE = Pattern.compile(" ");

    public static void sendMessage(String msg, int peerId, @NotNull String... attachments) {
        try {
            if (attachments.length != 0 || (msg != null && !msg.isEmpty())) {
                MessagesSendQuery query = send(peerId);
                if (msg != null && !msg.isEmpty()) {
                    query.message(msg);
                }
                if (attachments.length > 0) {
                    String array = COMPILE.matcher(Arrays.toString(attachments).substring(1)).replaceAll("");
                    query.attachment(array.substring(0, array.length()-1));
                }
                query.execute();
            }
        } catch (ApiException | ClientException ignored) {}
    }

    public static void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        try {
            send(peerId).keyboard(keyboard).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }

    public static void editChat(String title, int peerId) {
        try {
            vkApiClient.messages().editChat(groupActor, peerId - 2000000000, title).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    private static MessagesSendQuery send(int peerId) {
        return vkApiClient.messages()
                .send(groupActor)
                .randomId(MathUtils.RANDOM.nextInt(10000))
                .groupId(groupId)
                .peerId(peerId);
    }
}
