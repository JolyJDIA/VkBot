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
import jolyjdia.vk.api.callback.longpoll.CallbackApiLongPoll;
import jolyjdia.vk.api.client.VkApiClient;
import jolyjdia.vk.api.client.actors.GroupActor;
import jolyjdia.vk.api.objects.board.TopicComment;
import jolyjdia.vk.api.objects.messages.Message;
import jolyjdia.vk.api.objects.messages.MessageAction;
import jolyjdia.vk.api.objects.messages.MessageActionStatus;
import jolyjdia.vk.api.objects.photos.Photo;
import jolyjdia.vk.api.objects.wall.Wallpost;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {
  //  private final CommandExecutor executor = new CommandExecutor(Runtime.getRuntime().availableProcessors());

    CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
        super(client, actor);
    }

    @Override
    public final void messageNew(int groupId, @NotNull Message msg) {
        System.out.println(msg);
        if(msg.getPeerId() == msg.getFromId() || msg.getFromId() < 0) {
            return;
        }
        MessageAction action = msg.getAction();
        if(action != null) {
            MessageActionStatus type = action.getType();
            if(type == MessageActionStatus.CHAT_KICK_USER) {
                Bot.getUserBackend().deleteUser(msg.getPeerId(), msg.getFromId());
                //Удаляю из бд беседу

                submitEvent(new UserLeaveEvent(msg.getPeerId(), msg.getFromId()));
                return;
            } else if(type == MessageActionStatus.CHAT_INVITE_USER || type == MessageActionStatus.CHAT_INVITE_USER_BY_LINK) {
                submitEvent(new UserJoinEvent(msg.getPeerId(), msg.getFromId()));
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
        submitEvent(new NewMessageEvent(user, msg));
    }
    @Override
    public final void messageReply(int groupId, @NotNull Message msg) {
        if(msg.getPeerId() == msg.getFromId() || msg.getFromId() < 0) {
            return;
        }
        submitEvent(new ReplyMessageEvent(Bot.getUserBackend().addIfAbsentAndReturnUser(msg.getPeerId(), msg.getFromId()), msg));
    }

    @Override
    public final void messageEdit(int groupId, @NotNull Message msg) {
        if(msg.getPeerId() == msg.getFromId() || msg.getFromId() < 0) {
            return;
        }
        submitEvent(new EditMessageEvent(Bot.getUserBackend().addIfAbsentAndReturnUser(msg.getPeerId(), msg.getFromId()), msg));
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
        submitEvent(new NewPostWallEvent(wallpost));
    }
    @Override
    public final void wallRepost(int groupId, Wallpost wallpost) {
        submitEvent(new RepostWallEvent(wallpost));
    }
    @Override
    public final void boardPostNew(int groupId, TopicComment comment) {
        submitEvent(new BoardPostNewEvent(comment));
    }
    @Override
    public final void boardPostEdit(int groupId, TopicComment comment) {
        submitEvent(new BoardPostEditEvent(comment));
    }
    @Override
    public final void boardPostRestore(int groupId, TopicComment comment) {
        submitEvent(new BoardPostRestoreEvent(comment));
    }
    private static void submitEvent(@NotNull Event event) {
        Bot.getBotManager().getListeners().forEach(handler -> handler.accept(event));
    }
}