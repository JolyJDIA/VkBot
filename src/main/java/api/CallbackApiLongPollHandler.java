package api;

import api.command.RegisterCommandList;
import api.entity.User;
import api.event.Event;
import api.event.EventHandler;
import api.event.RegisterListEvent;
import api.event.board.BoardPostEditEvent;
import api.event.board.BoardPostNewEvent;
import api.event.board.BoardPostRestoreEvent;
import api.event.messages.EditMessageEvent;
import api.event.messages.NewMessageEvent;
import api.event.messages.ReplyMessageEvent;
import api.event.messages.SendCommandEvent;
import api.event.post.NewPostWallEvent;
import api.event.post.RepostWallEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.board.TopicComment;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.wall.Wallpost;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {

  //  private final CommandExecutor executor = new CommandExecutor(Runtime.getRuntime().availableProcessors());

    public CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
        super(client, actor);
    }

    @Override
    public final void messageNew(Integer groupId, @NotNull Message msg) {
        ObedientBot.SCHEDULER.scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("dasdasdasdas");
            }
        },5,  2);
        @NonNls String text = msg.getText();//УБрать аннотацию
        if(text.length() > 1 && (text.charAt(0) == '/' || text.charAt(0) == '!')) {//Проверяю первый символ(startsWith abort)
            String[] args = text.substring(1).split(" ");//убираю '/' и получаю аргументы
            User user = Bot.getProfileList().addIfAbsentAndReturn(msg.getPeerId(), msg.getFromId());

            long start = System.currentTimeMillis();
            RegisterCommandList.getRegisteredCommands().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(args[0])
                            || c.getAlias() != null && !c.getAlias().isEmpty()
                            && c.getAlias().stream().anyMatch(e -> e.equalsIgnoreCase(args[0])))
                    .findFirst()
                    .ifPresent(c -> c.execute(user, args));

            @NonNls long end = System.currentTimeMillis() - start;
            System.out.println("КОМАНДА: "+ Arrays.toString(args) +" ВЫПОЛНИЛАСЬ ЗА: "+end+" миллисекунд");

            SendCommandEvent event = new SendCommandEvent(msg);
            fireEvent(event);
            return;
        }
        System.out.println("СООБЩЕНИЕ: ("+ msg.getText() + ") ЧАТ: " +msg.getPeerId());
        NewMessageEvent event = new NewMessageEvent(msg);
        fireEvent(event);
    }

    /**
     * @param groupId
     * @param msg
     */
    @Override
    public final void messageReply(Integer groupId, Message msg) {
        ReplyMessageEvent event = new ReplyMessageEvent(msg);
        fireEvent(event);
    }

    @Override
    public final void messageEdit(Integer groupId, Message msg) {
        EditMessageEvent event = new EditMessageEvent(msg);
        fireEvent(event);
    }
    @Override
    public final void wallPostNew(Integer groupId, Wallpost wallpost) {
        NewPostWallEvent event = new NewPostWallEvent(wallpost);
        fireEvent(event);
    }
    @Override
    public final void wallRepost(Integer groupId, Wallpost wallpost) {
        RepostWallEvent event = new RepostWallEvent(wallpost);
        fireEvent(event);
    }
    @Override
    public final void boardPostNew(Integer groupId, TopicComment comment) {
        BoardPostNewEvent event = new BoardPostNewEvent(comment);
        fireEvent(event);
    }
    @Override
    public final void boardPostEdit(Integer groupId, TopicComment comment) {
        BoardPostEditEvent event = new BoardPostEditEvent(comment);
        fireEvent(event);
    }
    @Override
    public final void boardPostRestore(Integer groupId, TopicComment comment) {
        BoardPostRestoreEvent event = new BoardPostRestoreEvent(comment);
        fireEvent(event);
    }
    private static void fireEvent(Event event) {
        RegisterListEvent.getRegisteredListeners().forEach(listener -> {//Получаю все зарегистрированные классы событий
            for (Method m : listener.getClass().getDeclaredMethods()) {
                if (!m.isAnnotationPresent(EventHandler.class)) {
                    continue;
                }
                if (!event.getClass().isAssignableFrom(m.getParameterTypes()[0])) {
                    continue;
                }
                try {
                    m.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}