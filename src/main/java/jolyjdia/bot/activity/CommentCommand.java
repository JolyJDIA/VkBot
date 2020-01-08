package jolyjdia.bot.activity;

import com.google.common.collect.Lists;
import jolyjdia.api.command.Command;
import jolyjdia.api.scheduler.RoflanRunnable;
import jolyjdia.api.storage.Chat;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.MathUtils;
import jolyjdia.api.utils.StringBind;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import vk.client.actors.UserActor;
import vk.exceptions.ApiException;
import vk.exceptions.ClientException;
import vk.objects.photos.Photo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class CommentCommand extends Command {

    public static final String[] ALBUMS = {"wall", "profile"};
    private static final Pattern COMPILE = Pattern.compile(", ");

    public static final String[] COMMENTS = {
            "И этому дрочат в России?",
            "Такой с одной тычке ляжет",
            "малолетний дебил",
            "он придумал ебаться в жопу",
            "Кринж",
            "Удаляй страничку",
            "С головой пиздец",
            "вот дурашка забыл таблетки от шизы принять",
            "Аллах так сказал",
            "тупые насти как же я вас блин ненавижу"
    };

    protected CommentCommand() {
        super("comment");
    }

    @Override
    public void execute(User sender, @NotNull String[] args) {
        if(args.length >= 3) {
            int userId = Integer.parseInt(args[2]);
            String[] albums = ALBUMS;
            if(args.length >= 4) {
                albums = COMPILE.split(StringBind.toString(3, args));
            }
            List<Photo> photos = Lists.newArrayList();
            for (String id : albums) {
                try {
                    photos.addAll(Objects.requireNonNull(Bot.getVkApiClient().photos().get(VkUtils.ZAVR)
                            .ownerId(userId)
                            .albumId(id)
                            .execute())
                            .getItems());
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }
            }
            Collections.shuffle(photos);
            Iterator<Photo> iterator = photos.iterator();
            @NonNls CommentRunnable runnable = new CommentRunnable(iterator, userId, sender.getChat());
            if(args[1].equalsIgnoreCase("add")) {
                runnable.setAdd(true);
            }
            sender.getChat().sendMessage("Комменты: Start CommentRunnable: "+runnable);
            runnable.runTaskTimerAsynchronously(0, 120);
        }
    }

    private static final class CommentRunnable extends RoflanRunnable {
        private static final UserActor[] USER_ACTORS = {VkUtils.ZAVR, VkUtils.VALERA};
        private final Iterator<? extends Photo> iterator;
        private final int userId;
        private final Chat<?> chat;
        private boolean add;

        public CommentRunnable(Iterator<? extends Photo> iterator, int userId, Chat<?> chat) {
            this.iterator = iterator;
            this.userId = userId;
            this.chat = chat;
        }

        @Override
        public void run() {

            if(iterator.hasNext()) {
                int item = iterator.next().getId();
                try {
                    push(item);
                } catch (ApiException | ClientException e) {
                    chat.sendMessage("Комменты: Тут какие-то шоколадки");
                    cancel();
                }
            } else {
                chat.sendMessage("Комменты: Completed! Удаляю таску...");
                cancel();
            }
        }
        public void push(int itemId) throws ClientException, ApiException {
            if(add) {
                Bot.getVkApiClient().photos()
                        .createComment(VkUtils.ZAVR, itemId)
                        .ownerId(userId)
                        .message(COMMENTS[MathUtils.RANDOM.nextInt(COMMENTS.length)])
                        .execute();
            } else {
                Bot.getVkApiClient().photos()
                        .deleteComment(VkUtils.ZAVR, itemId)
                        .ownerId(userId).execute();
            }

        }
        public boolean isAdd() {
            return add;
        }
        public void setAdd(boolean add) {
            this.add = add;
        }
    }
}
