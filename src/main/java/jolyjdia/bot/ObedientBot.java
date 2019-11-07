package jolyjdia.bot;

import api.Bot;
import api.BotManager;
import api.RoflanBot;
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
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import jolyjdia.bot.calculate.CalculatorRegister;
import jolyjdia.bot.geo.GeoLoad;
import jolyjdia.bot.newcalculator.InitCalc;
import jolyjdia.bot.puzzle.Puzzle;
import jolyjdia.bot.smile.SmileLoad;
import jolyjdia.bot.translator.YandexTraslate;
import jolyjdia.bot.utils.GeneratorPassword;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public final class ObedientBot implements RoflanBot {
    private final BotScheduler scheduler = new BotScheduler();
    private final BotManager manager = new BotManager();
    private final Properties properties = new Properties();
    private final ModuleLoader moduleLoader = new ModuleLoader();
    private final HelpAllCommands helpCommand = new HelpAllCommands();
    private final UserBackend userBackend;
    private final String accessToken;
    private final int groupId;
    private final GroupActor groupActor;

    ObedientBot() throws ClientException, ApiException {
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

        PermissionManager.registerPermissionGroups();
        Bot.setBot(this);
        this.userBackend = properties.getProperty("mysql").equalsIgnoreCase("true") ?
                new MySQL(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("url")) :
                new ProfileList(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\users.json"));

        Groups groups = Loader.getVkApiClient().groups();
        if (!groups.getLongPollSettings(groupActor, groupActor.getGroupId()).execute().getIsEnabled()) {
            groups.setLongPollSettings(groupActor, groupActor.getGroupId())
                    .enabled(true)
                    .apiVersion("5.101")
                    .wallPostNew(true)
                    .messageNew(true)
                    .audioNew(true)
                    .groupJoin(true)
                    .groupLeave(true)
                    .messageReply(true)
                    .messageEdit(true)
                    .messageAllow(true)
                    .messageDeny(true)
                    .messageTypingState(true)
                    .execute();
        }
        registerModules();
        loadModule();
    }
    private void registerModules() {
        moduleLoader.registerModule(new CalculatorRegister());
        moduleLoader.registerModule(new YandexTraslate());
        moduleLoader.registerModule(new GeoLoad());
        moduleLoader.registerModule(new Puzzle());
        moduleLoader.registerModule(new GeneratorPassword());
        moduleLoader.registerModule(new SmileLoad());
        moduleLoader.registerModule(new InitCalc());
        //moduleLoader.registerModule(new ShoutboxMain());
        //moduleLoader.registerModule(new CraftClient());
    }
    private void loadModule() {
        moduleLoader.getModules().forEach(Module::onLoad);
        helpCommand.initializeHelp();
    }

    @Contract(pure = true)
    @Override
    public HelpAllCommands getHelpCommand() {
        return helpCommand;
    }

    @Contract(pure = true)
    @Override
    public ModuleLoader getModuleLoader() {
        return moduleLoader;
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
    public UserBackend getUserBackend() {
        return userBackend;
    }

    @Contract(pure = true)
    @Override
    public BotManager getBotManager() {
        return manager;
    }
    @Contract(pure = true)
    @Override
    public BotScheduler getScheduler() {
        return scheduler;
    }

    /**
     * @param msg
     * @param peerId
     * @param attachments
     */

    private static final Pattern COMPILE = Pattern.compile(" ");
    @Override
    public void sendMessage(String msg, int peerId, @NotNull String... attachments) {
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
    @Override
    public void sendKeyboard(String msg, int peerId, Keyboard keyboard) {
        try {
            send(peerId).keyboard(keyboard).message(msg).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    @Override
    public void editChat(String title, int peerId) {
        try {
            Loader.getVkApiClient().messages().editChat(Bot.getGroupActor(), peerId - 2000000000, title).execute();
        } catch (ApiException | ClientException ignored) {}
    }
    private MessagesSendQuery send(int peerId) {
        return Loader.getVkApiClient().messages()
                .send(groupActor)
                .randomId(MathUtils.RANDOM.nextInt(10000))
                .groupId(groupId)
                .peerId(peerId);
    }
}
