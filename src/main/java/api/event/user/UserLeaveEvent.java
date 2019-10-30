package api.event.user;

public class UserLeaveEvent extends UserEvent {

    public UserLeaveEvent(int peerId, int userId) {
        super(peerId, userId);
    }
}
