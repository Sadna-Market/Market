package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.StoreModel.Store;

import java.util.HashMap;

public class UserManager {
    HashMap<Integer,User> users;

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
        return UserId>0;
    }

    public User getUser(int UserId){
        return null;
    }

    public void addFounder(int userId, Store store) {
        //users.get(userId).addFounder(store);
    }

    public boolean isOwner(int userId, int storeId) {
        return userId>0;
    }

    public boolean addNewStoreOwner(int userId, Store store, int newOwnerId) {
        if(isOwner(userId,store.getStoreId())){
            User newOwner = users.get(newOwnerId);
            return newOwner.addNewStoreOwner(users.get(userId),store);
        }
        return false;
    }

    public boolean addNewStoreManager(int userId, Store store, int newMangerId) {
        if(isOwner(userId,store.getStoreId())){
            User newManager = users.get(newMangerId);
            return newManager.addNewStoreManager(users.get(userId),store);
        }
        return false;
    }

    public boolean setManagerPermissions(int userId, Store store, int managerId) {
        if(isOwner(userId,store.getStoreId())){
            User Manager = users.get(managerId);
            return Manager.setManagerPermissions(users.get(userId),store);
        }
        return false;
    }

    public boolean getRolesInStore(int userId, Store store) {
        return users.get(userId).getRolesInStore(store);
    }
}
