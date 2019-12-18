package api.storage;

import api.utils.cache.Cache;
import api.utils.cache.CacheBuilder;
import api.utils.cache.RemovalListener;
import jolyjdia.bot.Bot;

import java.util.concurrent.TimeUnit;

public class ChatCacheMySQL extends Chat<Cache<Integer, User>> {

    public ChatCacheMySQL(int peerId) {
        super(CacheBuilder.newBuilder()
                .expireAfterWrite(25, TimeUnit.MINUTES)
                .ticker(3)
                .removeListener((RemovalListener<Integer, User>) entry -> {
                    User user = entry.getValue();
                    System.out.println("Удаляю " + user.getUserId() + ' ' +user.getGroup().getName());
                    if (user.unchanged()) {
                        return;
                    }
                    System.out.println("Сохраняю" + user.getUserId() + ' ' +user.getGroup().getName());
                    Bot.getUserBackend().saveOrUpdateGroup(user);
                }).build(), peerId);
    }
}
