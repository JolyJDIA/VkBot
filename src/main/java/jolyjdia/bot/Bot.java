package jolyjdia.bot;

import api.BotManager;
import api.command.HelpAllCommands;
import api.module.Module;
import api.module.ModuleLoader;
import api.permission.PermissionManager;
import api.scheduler.BotScheduler;
import api.storage.JsonBackend;
import api.storage.MySqlBackend;
import api.storage.UserBackend;
import com.vk.api.sdk.actions.Groups;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.activity.ActivityLoad;
import jolyjdia.bot.calculator.CalculatorManager;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.smile.SmileLoad;
import jolyjdia.bot.translator.YandexTraslate;
import jolyjdia.bot.utils.MyHttpTransportClient;
import jolyjdia.bot.utils.UtilsModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Bot {
    private static final VkApiClient vkApiClient = new VkApiClient(new MyHttpTransportClient());
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

        PermissionManager.newInstance();
        userBackend = properties.getProperty("mysql").equalsIgnoreCase("true") ?
                MySqlBackend.of(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("url")) :
                new JsonBackend(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));

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
        moduleLoader.getModules().forEach(Module::onLoad);
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
}
