package jolyjdia.bot;

import jolyjdia.api.BotManager;
import jolyjdia.api.command.HelpAllCommands;
import jolyjdia.api.module.ModuleLoader;
import jolyjdia.api.permission.PermissionManager;
import jolyjdia.api.scheduler.BotScheduler;
import jolyjdia.api.storage.JsonBackend;
import jolyjdia.api.storage.MySqlBackend;
import jolyjdia.api.storage.UserBackend;
import jolyjdia.bot.activity.ActivityLoad;
import jolyjdia.bot.calculator.CalculatorManager;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.smile.SmileLoad;
import jolyjdia.bot.translator.YandexTraslate;
import jolyjdia.bot.utils.UtilsModule;
import jolyjdia.vk.api.actions.Groups;
import jolyjdia.vk.api.callback.longpoll.CallbackApiLongPoll;
import jolyjdia.vk.api.client.VkApiClient;
import jolyjdia.vk.api.client.actors.GroupActor;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.httpclient.SyncHttpTransportClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Bot {
    private static final VkApiClient vkApiClient = new VkApiClient(new SyncHttpTransportClient());
    private static final BotScheduler scheduler = new BotScheduler();
    private static final BotManager manager = new BotManager();
    private static final Properties properties = new Properties();
    private static final ModuleLoader moduleLoader = new ModuleLoader();
    private static final HelpAllCommands helpCommand = new HelpAllCommands();
    private static final UserBackend userBackend;
    private static final int groupId;
    private static final String accessToken;
    private static final GroupActor groupActor;
    private static final CallbackApiLongPoll longPoll;
    //static UserActor bolvan = new UserActor(150250636, "3ddf24d5927e346124c52ae9e80e41c94a6d0949184b1d343ab1d2f0662a3878041fa4190efe9368606b8");

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
        longPoll = new CallbackApiLongPollHandler(vkApiClient, groupActor);
        PermissionManager.newInstance();
        userBackend = properties.getProperty("mysql").equalsIgnoreCase("true") ?
                MySqlBackend.of(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("url")) :
                new JsonBackend(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));

        Groups groups = vkApiClient.groups();
        try {
            if (!Objects.requireNonNull(groups.getLongPollSettings(groupActor, groupActor.getGroupId()).execute()).getIsEnabled()) {
                groups.setLongPollSettings(groupActor, groupActor.getGroupId())
                        .enabled(true).apiVersion("5.103").wallPostNew(true).messageNew(true)
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
    private Bot() {}

    private static void registerModules() {
        moduleLoader.registerModule(new YandexTraslate());
        moduleLoader.registerModule(new GeoLoad());
        moduleLoader.registerModule(new Puzzle());
        moduleLoader.registerModule(new UtilsModule());
        moduleLoader.registerModule(new SmileLoad());
        moduleLoader.registerModule(new CalculatorManager());
        moduleLoader.registerModule(new ActivityLoad());
    }
    private static void loadModule() {
        moduleLoader.enableModule();
        helpCommand.initializeHelp();
    }

    public static HelpAllCommands getHelpCommand() {
        return helpCommand;
    }

    public static ModuleLoader getModuleLoader() {
        return moduleLoader;
    }

    public static GroupActor getGroupActor() {
        return groupActor;
    }

    public static int getGroupId() {
        return groupId;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static Properties getConfig() {
        return properties;
    }

    public static UserBackend getUserBackend() {
        return userBackend;
    }

    public static BotManager getBotManager() {
        return manager;
    }

    public static BotScheduler getScheduler() {
        return scheduler;
    }

    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }

    public static CallbackApiLongPoll getLongPoll() {
        return longPoll;
    }
}
