package jolyjdia.bot.activity;

import com.google.common.collect.Lists;
import jolyjdia.api.command.Command;
import jolyjdia.api.scheduler.RoflanRunnable;
import jolyjdia.api.storage.Chat;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.bot.Bot;
import jolyjdia.vk.api.client.actors.UserActor;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.objects.likes.Type;
import jolyjdia.vk.api.objects.photos.Photo;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LikesCommand extends Command {
    public static final String[] ALBUMS = {"wall", "profile"};
    private static final Pattern COMPILE = Pattern.compile(", ");

    protected LikesCommand() {
        super("likes");
    }

    @Override
    public final void execute(User sender, @NotNull String[] args) {
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
            Iterator<Photo> iterator = photos.iterator();
            @NonNls LikesRunnable runnable = new LikesRunnable(iterator, userId, sender.getChat());
            if(args[1].equalsIgnoreCase("add")) {
                runnable.setAdd(true);
            }
            sender.getChat().sendMessage("Лайки: Start LikesRunnable: "+runnable);
            runnable.runRepeatingAsyncTaskAfter(0, 60);
        }
    }

    private static final class LikesRunnable extends RoflanRunnable {
        private static final UserActor[] USER_ACTORS = {VkUtils.ZAVR, VkUtils.VALERA};
        private final Iterator<? extends Photo> iterator;
        private final int userId;
        private final Chat<?> chat;
        private boolean add;

        public LikesRunnable(Iterator<? extends Photo> iterator, int userId, Chat<?> chat) {
            this.iterator = iterator;
            this.userId = userId;
            this.chat = chat;
        }

        @Override
        public void run() {

            if(iterator.hasNext()) {
                Photo item = iterator.next();
                try {
                    push(item.getId());
                } catch (ApiException | ClientException e) {
                    chat.sendMessage("Лайки: Тут какие-то шоколадки");
                    cancel();
                }
            } else {
                chat.sendMessage("Лайки: Completed! Удаляю таску...");
                cancel();
            }
        }
        /**
         * @param itemId
         * @throws ClientException
         * @throws ApiException
         */
        public void push(int itemId) {
            for(UserActor user : USER_ACTORS) {
                if(add) {
                    Bot.getVkApiClient().likes().add(user, Type.PHOTO, itemId)
                            .ownerId(userId)
                            .execute();
                } else {
                    Bot.getVkApiClient().likes().delete(user, Type.PHOTO, itemId)
                            .ownerId(userId)
                            .execute();
                }
            }
        }
        public boolean isAdd() {
            return add;
        }
        public void setAdd(boolean add) {
            this.add = add;
        }
    }
    public enum TypeAlbum {
        WALL("wall"),
        SAVED("saved"),
        PROFILE("profile");

        private final String name;
        TypeAlbum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
