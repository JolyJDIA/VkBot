package api.event.user;

public class UserJoinEvent extends UserEvent {

    public UserJoinEvent(int peerId, int userId) {
        super(peerId, userId);
    }
}
