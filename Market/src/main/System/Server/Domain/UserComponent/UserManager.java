package main.System.Server.Domain.UserComponent;

public class UserManager {


    public boolean Login(String email, int password) {
        return false;
    }

    public boolean Logout(int userId) {
        return false;
    }

    public boolean GuestVisit() {
        return false;
    }

    public boolean GuestLeave(int guestId) {
        return false;
    }

    public boolean AddNewMember(String email, int Password) {
        return false;
    }

    public boolean isLogin(int UserId){
        return false;
    }

    public User getUser(int UserId){
        return null;
    }

}
