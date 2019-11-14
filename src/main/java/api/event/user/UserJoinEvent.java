package api.event.user;

public class UserJoinEvent extends UserEvent {

    //ВОЗМОЖНО ПОТОМ ИЗМЕНЮ ПАРАМЕТРЫ НА User
    public UserJoinEvent(int peerId, int userId) {
        super(peerId, userId);
    }
}
