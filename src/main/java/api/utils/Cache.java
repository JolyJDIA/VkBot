package api.utils;

import api.entity.Chat;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Cache {
    private final LoadingCache<Integer, Chat> empCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                       @Override
                       public Chat load(@NotNull Integer id) {
                           return new Chat(id, new ArrayList<>());
                       }
                   }
            );
}
