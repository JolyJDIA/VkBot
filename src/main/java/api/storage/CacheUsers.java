package api.storage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CacheUsers {
    public LoadingCache<Integer, UserInfo> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(15)
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public UserInfo load(@NotNull Integer o) throws ClientException, ApiException {
                    return getUserInfo(o);
                }
            });
    public static @NotNull UserInfo getUserInfo(int id) throws ClientException, ApiException {
        UserXtrCounters users = Bot.getVkApiClient().users().get(Bot.getGroupActor()).userIds(String.valueOf(id)).execute().get(0);
        return new UserInfo(users.getFirstName(), users.getLastName());
    }

    public static class UserInfo {
        private final String name;
        private final String surname;

        @Contract(pure = true)
        public UserInfo(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public String getSurname() {
            return surname;
        }

        public String getName() {
            return name;
        }
    }
}
