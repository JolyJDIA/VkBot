package jolyjdia.bot;

import api.Bot;
import api.event.Event;
import api.event.board.BoardPostEditEvent;
import api.event.board.BoardPostNewEvent;
import api.event.board.BoardPostRestoreEvent;
import api.event.messages.EditMessageEvent;
import api.event.messages.NewMessageEvent;
import api.event.messages.ReplyMessageEvent;
import api.event.messages.SendCommandEvent;
import api.event.post.NewPostWallEvent;
import api.event.post.RepostWallEvent;
import api.storage.User;
import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.board.TopicComment;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAction;
import com.vk.api.sdk.objects.messages.MessageActionStatus;
import com.vk.api.sdk.objects.wall.Wallpost;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {
    @NonNls private static final Logger LOGGER = Logger.getLogger(CallbackApiLongPollHandler.class.getName());

  //  private final CommandExecutor executor = new CommandExecutor(Runtime.getRuntime().availableProcessors());

    CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
        super(client, actor, 25);
    }

    @Override
    public final void messageNew(Integer groupId, @NotNull Message msg) {
        if(msg.getPeerId().equals(msg.getFromId())) {
            return;
        }
        MessageAction action = msg.getAction();
        if(action != null && action.getType() == MessageActionStatus.CHAT_KICK_USER) {
            Bot.getProfileList().remove(msg.getPeerId(), msg.getFromId());
            return;
        }
        @NonNls String text = msg.getText();
        User user = Bot.getProfileList().addIfAbsentAndReturn(msg.getPeerId(), msg.getFromId());
        if(text.length() > 1 && (text.charAt(0) == '/' || text.charAt(0) == '!')) {
            String[] args = text.substring(1).split(" ");//убираю '/' и получаю аргументы
            SendCommandEvent event = new SendCommandEvent(user, args);
            submitEvent(event);
            if(event.isCancelled()) {
                return;
            }
            long start = System.currentTimeMillis();
            Bot.getBotManager().getRegisteredCommands().stream()
                    .filter(c -> {
                        Set<String> alias = c.getAlias();
                        return c.getName().equalsIgnoreCase(args[0])
                                || (alias != null && !alias.isEmpty()
                                && alias.stream().anyMatch(e -> e.equalsIgnoreCase(args[0])));
                    })
                    .findFirst()
                    .ifPresent(c -> c.execute(user, args));
            @NonNls long end = System.currentTimeMillis() - start;
            LOGGER.log(Level.INFO, "КОМАНДА: "+ Arrays.toString(args) +" ВЫПОЛНИЛАСЬ ЗА: "+end+" миллисекунд");
            return;
        }
        System.out.println("СООБЩЕНИЕ: ("+ msg.getText() + ") ЧАТ: " +msg.getPeerId());
        NewMessageEvent event = new NewMessageEvent(user, msg);
        submitEvent(event);
    }

    @Override
    public final void messageReply(Integer groupId, @NotNull Message msg) {
        if(msg.getPeerId().equals(msg.getFromId())) {
            return;
        }
        User user = Bot.getProfileList().addIfAbsentAndReturn(msg.getPeerId(), msg.getFromId());
        ReplyMessageEvent event = new ReplyMessageEvent(user, msg);
        submitEvent(event);
    }

    @Override
    public final void messageEdit(Integer groupId, @NotNull Message msg) {
        if(msg.getPeerId().equals(msg.getFromId())) {
            return;
        }
        User user = Bot.getProfileList().addIfAbsentAndReturn(msg.getPeerId(), msg.getFromId());
        EditMessageEvent event = new EditMessageEvent(user, msg);
        submitEvent(event);
    }
    @Override
    public final void wallPostNew(Integer groupId, Wallpost wallpost) {
        NewPostWallEvent event = new NewPostWallEvent(wallpost);
        submitEvent(event);
    }
    @Override
    public final void wallRepost(Integer groupId, Wallpost wallpost) {
        RepostWallEvent event = new RepostWallEvent(wallpost);
        submitEvent(event);
    }
    @Override
    public final void boardPostNew(Integer groupId, TopicComment comment) {
        BoardPostNewEvent event = new BoardPostNewEvent(comment);
        submitEvent(event);
    }
    @Override
    public final void boardPostEdit(Integer groupId, TopicComment comment) {
        BoardPostEditEvent event = new BoardPostEditEvent(comment);
        submitEvent(event);
    }
    @Override
    public final void boardPostRestore(Integer groupId, TopicComment comment) {
        BoardPostRestoreEvent event = new BoardPostRestoreEvent(comment);
        submitEvent(event);
    }
    private static void submitEvent(@NotNull Event event) {
        Bot.getBotManager().getListeners().forEach(handler -> handler.accept(event));
    }
}