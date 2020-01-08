package jolyjdia.bot;

import jolyjdia.api.event.Event;
import jolyjdia.api.event.board.BoardPostEditEvent;
import jolyjdia.api.event.board.BoardPostNewEvent;
import jolyjdia.api.event.board.BoardPostRestoreEvent;
import jolyjdia.api.event.messages.EditMessageEvent;
import jolyjdia.api.event.messages.NewMessageEvent;
import jolyjdia.api.event.messages.ReplyMessageEvent;
import jolyjdia.api.event.messages.SendCommandEvent;
import jolyjdia.api.event.post.NewPostWallEvent;
import jolyjdia.api.event.post.RepostWallEvent;
import jolyjdia.api.event.user.UserJoinEvent;
import jolyjdia.api.event.user.UserLeaveEvent;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import jolyjdia.bot.smile.SmileLoad;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import vk.callback.longpoll.CallbackApiLongPoll;
import vk.client.VkApiClient;
import vk.client.actors.GroupActor;
import vk.objects.board.TopicComment;
import vk.objects.messages.Message;
import vk.objects.messages.MessageAction;
import vk.objects.messages.MessageActionStatus;
import vk.objects.photos.Photo;
import vk.objects.wall.Wallpost;

import java.util.Arrays;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {
  //  private final CommandExecutor executor = new CommandExecutor(Runtime.getRuntime().availableProcessors());

    CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
        super(client, actor);
    }

    @Override
    public final void messageNew(int groupId, @NotNull Message msg) {
        if(msg.getPeerId().equals(msg.getFromId()) || msg.getFromId() < 0) {
            return;
        }
        MessageAction action = msg.getAction();
        if(action != null) {
            MessageActionStatus type = action.getType();
            if(type == MessageActionStatus.CHAT_KICK_USER) {
                Bot.getUserBackend().deleteUser(msg.getPeerId(), msg.getFromId());
                //Удаляю из бд беседу

                UserLeaveEvent event = new UserLeaveEvent(msg.getPeerId(), msg.getFromId());
                submitEvent(event);
                return;
            } else if(type == MessageActionStatus.CHAT_INVITE_USER || type == MessageActionStatus.CHAT_INVITE_USER_BY_LINK) {
                UserJoinEvent event = new UserJoinEvent(msg.getPeerId(), msg.getFromId());
                submitEvent(event);
                return;
            }
        }
        @NonNls String text = msg.getText();
        User user = Bot.getUserBackend().addIfAbsentAndReturnUser(msg.getPeerId(), msg.getFromId());

        if(text.length() > 1 && (text.charAt(0) == '/' || text.charAt(0) == '!')) {
            String[] args = text.substring(1).split(" ");
            long start = System.currentTimeMillis();
            if(Bot.getBotManager().getRegisteredCommands().stream().noneMatch(cmd -> {
                if(cmd.equalsCommand(args[0])) {
                    SendCommandEvent event = new SendCommandEvent(user, args);
                    submitEvent(event);
                    if(!event.isCancelled()) {
                        cmd.execute(user, args);
                    }
                    return true;
                }
                return false;
            })) { return; }
            @NonNls long end = System.currentTimeMillis() - start;
            StringBind.log("Команда: "+ Arrays.toString(args) +" ("+end+"ms) Чат: " +msg.getPeerId() + '(' +msg.getFromId()+ ')');
            return;
        }
        StringBind.log("Сообщение: \""+ msg.getText()+ "\" Чат: " +msg.getPeerId() + '(' +msg.getFromId()+ ')');
        NewMessageEvent event = new NewMessageEvent(user, msg);
        submitEvent(event);
    }
    @Override
    public final void messageReply(int groupId, @NotNull Message msg) {
        if(msg.getPeerId().equals(msg.getFromId()) || msg.getFromId() < 0) {
            return;
        }
        User user = Bot.getUserBackend().addIfAbsentAndReturnUser(msg.getPeerId(), msg.getFromId());
        ReplyMessageEvent event = new ReplyMessageEvent(user, msg);
        submitEvent(event);
    }

    @Override
    public final void messageEdit(int groupId, @NotNull Message msg) {
        if(msg.getPeerId().equals(msg.getFromId()) || msg.getFromId() < 0) {
            return;
        }
        User user = Bot.getUserBackend().addIfAbsentAndReturnUser(msg.getPeerId(), msg.getFromId());
        EditMessageEvent event = new EditMessageEvent(user, msg);
        submitEvent(event);
    }
    @Override
    public final void photoNew(int groupId, @NotNull Photo photo) {
        if(SmileLoad.getInstance().getAlbumId() != photo.getAlbumId()) {
            return;
        }
        SmileLoad.getInstance().addSmile(photo);
    }
    @Override
    public final void wallPostNew(int groupId, Wallpost wallpost) {
        NewPostWallEvent event = new NewPostWallEvent(wallpost);
        submitEvent(event);
    }
    @Override
    public final void wallRepost(int groupId, Wallpost wallpost) {
        RepostWallEvent event = new RepostWallEvent(wallpost);
        submitEvent(event);
    }
    @Override
    public final void boardPostNew(int groupId, TopicComment comment) {
        BoardPostNewEvent event = new BoardPostNewEvent(comment);
        submitEvent(event);
    }
    @Override
    public final void boardPostEdit(int groupId, TopicComment comment) {
        BoardPostEditEvent event = new BoardPostEditEvent(comment);
        submitEvent(event);
    }
    @Override
    public final void boardPostRestore(int groupId, TopicComment comment) {
        BoardPostRestoreEvent event = new BoardPostRestoreEvent(comment);
        submitEvent(event);
    }
    private static void submitEvent(@NotNull Event event) {
        Bot.getBotManager().getListeners().forEach(handler -> handler.accept(event));
    }
}