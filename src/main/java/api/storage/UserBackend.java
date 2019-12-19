package api.storage;

import java.util.Optional;
import java.util.Set;

public interface UserBackend {//extends Map<Integer, Chat<?>> {
    User addIfAbsentAndReturn(int peerId, int userId);
    Set<Integer> getChats();
    Optional<User> getUser(int peerId, int userId);
    void deleteUser(int peerId, int userId);
    void deleteChat(int peerId);
    void saveAll();
    default void saveOrUpdateGroup(User user) {}
    Chat<?> getChat(int peerId);
}
