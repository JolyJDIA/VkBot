package api.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import jolyjdia.bot.Bot;

import java.util.concurrent.TimeUnit;

public class Chat {
    private final int peerId;
    private boolean access;
    private final Cache<Integer, User> users = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(20, TimeUnit.MINUTES)//20 минут
            .removalListener((RemovalListener<Integer, User>) notification -> {
                User user = notification.getValue();
                System.out.println("Удаляю " + user.getUserId());
                if (!user.isChange()) {
                      return;
                }
                Bot.getUserBackend().saveOrUpdateGroup(user);
                user.setChange(false);
            }).build();

    public Chat(int peerId) {
        this.peerId = peerId;
    }
    public Chat(int peerId, boolean access) {
        this.peerId = peerId;
        this.access = access;
    }
    public final boolean isAccess() {
        return access;
    }

    public final Cache<Integer, User> getUsers() {
        return users;
    }

    public final int getPeerId() {
        return peerId;
    }
}
