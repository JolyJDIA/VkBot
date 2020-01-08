package jolyjdia.api.event.user;

public class UserLeaveEvent extends UserEvent {
    //ВОЗМОЖНО ПОТОМ ИЗМЕНЮ ПАРАМЕТРЫ НА User
    public UserLeaveEvent(int peerId, int userId) {
        super(peerId, userId);
    }
}
