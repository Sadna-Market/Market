package com.example.demo.Domain.UserModel;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.PermissionManager;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.Market.userTypes;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.AlertService.AlertService;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;
//*

/**
 * userManger mange all users in the system its saved all the users that sign in to the system ,users that logged and all current guests
 * then someone enter to the system the system will generate an a unique userID,
 * the user id will not saved in the db
 * the user id is used only for manging the current online visitor : Guests , Members
 */


public class UserManager {

    private AlertService alertService;

    private StampedLock LockUsers = new StampedLock();

    /**
     * menage all the members that have a saved user in the system , key : Emile , value : User
     */
    ConcurrentHashMap<String, User> members;  // menage all the members that have a saved user in the system , key : Emile , value : User
    /**
     * menage all the Visitors in the System , Visitors is set combines a members and gusts. key : userId , value Guest
     */
    ConcurrentHashMap<UUID, User> GuestVisitors;
    /**
     * menage all the logged in users in the system, key : user id  , value : User
     */
    ConcurrentHashMap<UUID, User> LoginUsers;

    static Logger logger = Logger.getLogger(ShoppingBag.class);

    public UserManager() {
        members = new ConcurrentHashMap<>();
        GuestVisitors = new ConcurrentHashMap<>();
        LoginUsers = new ConcurrentHashMap<>();
        alertService = new AlertService();
    }

    public DResponseObj<Boolean> isMember(String email) {
        return members.containsKey(email) ? new DResponseObj<>(true, -1) : new DResponseObj<>(false, ErrorCode.NOTMEMBER);
    }

    public DResponseObj<UUID> GuestVisit() {
        logger.debug("UserManager GuestVisit");
        UUID newid = UUID.randomUUID();
        User guest = new User();
        GuestVisitors.put(newid, guest);
        return new DResponseObj<>(newid);
    }

    public DResponseObj<Boolean> isOwner(String email, Store store) {
        if (!members.containsKey(email)) {
            return new DResponseObj<>(ErrorCode.NOTMEMBER);
        }
        User u = members.get(email);
        logger.debug("UserManager isOwner");
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<userTypes> res = permissionManager.getGranteeUserType(u, store);
        return res.value.equals(userTypes.owner) ? new DResponseObj<>(true, -1) : new DResponseObj<>(false, ErrorCode.NOTOWNER);

    }

    public DResponseObj<Boolean> isManager(String email, Store store) {
        if (!members.containsKey(email)) {
            return new DResponseObj<>(ErrorCode.NOTMEMBER);
        }
        User u = members.get(email);
        logger.debug("UserManager isManager");
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<userTypes> res = permissionManager.getGranteeUserType(u, store);
        return res.value.equals(userTypes.manager) ? new DResponseObj<>(true, -1) : new DResponseObj<>(false, ErrorCode.NOT_MANAGER);

    }

    public DResponseObj<Boolean> GuestLeave(UUID guestId) {
        logger.debug("UserManager GuestLeave");
        if (!GuestVisitors.containsKey(guestId)) {
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
            a.errorMsg = ErrorCode.NOTONLINE;
            return a;
        } else {
            GuestVisitors.remove(guestId);
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(true);

            return a;
        }
    }


    public DResponseObj<UUID> Login(UUID userID, String email, String password) {
        logger.debug("UserManager Login");
        if (GuestVisitors.containsKey(userID) && members.containsKey(email) && !LoginUsers.containsKey(userID) && members.get(email).isPasswordEquals(password).value) {
            User LogUser = members.get(email);
            UUID newMemberUUid = UUID.randomUUID();
            LoginUsers.put(newMemberUUid, LogUser);
            GuestVisitors.remove(userID);
            alertService.modifyDelayIfExist(LogUser.email,newMemberUUid);
            return new DResponseObj<>(newMemberUUid);
        } else {
            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
            return a;
        }

    }

    public DResponseObj<Boolean> ishasSystemManager() {
        for (String mail : members.keySet()) {
            if (PermissionManager.getInstance().isSystemManager(mail).value) {
                return new DResponseObj<>(true);
            }
        }
        return new DResponseObj<>(false);
    }

    public DResponseObj<UUID> Logout(UUID userId) {
        if (LoginUsers.containsKey(userId)) {
            LoginUsers.remove(userId);
            User guest = new User();
            UUID newMemberUUid = UUID.randomUUID();
            GuestVisitors.put(newMemberUUid, guest);
            return new DResponseObj<>(newMemberUUid);
        }
        DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOTLOGGED);
        return a;
    }


    public DResponseObj<Boolean> AddNewMember(UUID uuid, String email, String Password, String phoneNumber, LocalDate dateOfBirth) {

        long stamp = LockUsers.writeLock();
        try {
            if (!isOnline(uuid).value) {
                DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
                a.errorMsg = ErrorCode.NOTONLINE;
                return a;
            }
            if (members.containsKey(email)) {

                DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
                a.errorMsg = ErrorCode.NOTMEMBER;
                return a;
            }
            DResponseObj<Boolean> res = Validator.isValidPassword(Password);
            if (res.value == false) {
                return new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
            }
            res = Validator.isValidEmailAddress(email);
            if (res.value == false) {
                return new DResponseObj<>(ErrorCode.NOT_VALID_EMILE);
            }
            res = Validator.isValidPhoneNumber(phoneNumber);
            if (res.value == false) {
                return new DResponseObj<>(ErrorCode.NOT_VALID_PHONE);
            }

            User user = new User(email, Password, phoneNumber, dateOfBirth);
            members.put(email, user);
            return new DResponseObj<>(true);
        } finally {
            LockUsers.unlockWrite(stamp);
        }

    }

    public DResponseObj<Boolean> isFounder(Store store, String email) {
        if (!members.containsKey(email)) {
            return new DResponseObj<Boolean>(false, ErrorCode.NOTMEMBER);
        }
        return PermissionManager.getInstance().isFounder(members.get(email), store);
    }


    public DResponseObj<Boolean> isOwner(UUID user, Store store) {
        if (!isLogged(user).value) {
            return new DResponseObj<>(false, ErrorCode.NOTLOGGED);
        }
        User u = LoginUsers.get(user);
        logger.debug("UserManager isOwner");
        PermissionManager permissionManager = PermissionManager.getInstance();
        return new DResponseObj<>(permissionManager.getGranteeUserType(u, store).value.equals(userTypes.owner));
    }

    public DResponseObj<Boolean> addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        logger.debug("UserManager addNewStoreOwner");
        if (isLogged(userId).value) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(userId, store).value) {
                User newOwner = members.get(newOwnerEmail);
                return loggedUser.addNewStoreOwner(newOwner, store);
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }

    public DResponseObj<Boolean> isCartExist(UUID uuid) {
        if (GuestVisitors.containsKey(uuid)) {
            return new DResponseObj<>(true);
        }
        if (LoginUsers.containsKey(uuid)) {
            return new DResponseObj<>(true);
        }
        return new DResponseObj<>(false, ErrorCode.CART_FAIL);
    }

    public DResponseObj<Boolean> addFounder(UUID userId, Store store) {
        logger.debug("UserManager addFounder");

        if (isLogged(userId).value) {
            User user = LoginUsers.get(userId);
            return user.addFounder(store);
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }

    public DResponseObj<Boolean> addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        logger.debug("UserManager addNewStoreManager");

        if (isLogged(uuid).value) {
            User loggedUser = LoginUsers.get(uuid);
            if (isOwner(uuid, store).value) {
                User newManager = members.get(newMangerEmail);
                return loggedUser.addNewStoreManager(newManager, store);
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }


    public DResponseObj<Boolean> setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm, boolean onof) {
        logger.debug("UserManager setManagerPermissions");
        if (isLogged(userId).value) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(userId, store).value) {
                User Manager = members.get(email);
                DResponseObj<Boolean> res =  loggedUser.setManagerPermissions(Manager, store, perm, onof);
                if(res.errorOccurred()) return res;
                logger.info(String.format("notifying %s for his permission setup", Manager.email));
                String msg = String.format("Your permission %s was %s store %d by %s", perm.toString(),
                        onof ? "added to" : "removed from", store.getStoreId().value, loggedUser.email);
                notifyUsers(List.of(Manager),msg);
                return res;
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }


    /**
     * return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public DResponseObj<Boolean> isLogged(UUID uuid) {
        logger.debug("UserManager isLogged");
        if (LoginUsers.containsKey(uuid)) {
            return new DResponseObj<>(true, -1);
        } else {
            return new DResponseObj<>(false, ErrorCode.NOTLOGGED);
        }
    }
    /**
     * return true if existing member is logged in the system
     * @param username
     * @return boolean
     */
    public DResponseObj<UUID> isLogged(String username) {
        logger.debug("UserManager isLogged");
        for(Map.Entry<UUID,User> entry : LoginUsers.entrySet()){
            if(entry.getValue().email.equals(username))
                return new DResponseObj<>(entry.getKey(),-1);
        }
        return new DResponseObj<>(null,ErrorCode.NOTLOGGED);
    }

    public DResponseObj<ShoppingCart> getUserShoppingCart(UUID userId) {
        logger.debug("UserManager getUserShoppingCart");

        if (GuestVisitors.containsKey(userId)) {
            return new DResponseObj<ShoppingCart>(GuestVisitors.get(userId).GetSShoppingCart().value, -1);
        } else if (LoginUsers.containsKey(userId)) {
            return new DResponseObj<ShoppingCart>(LoginUsers.get(userId).GetSShoppingCart().value, -1);
        }
        DResponseObj<ShoppingCart> a = new DResponseObj<>();
        a.errorMsg = ErrorCode.NOTONLINE;
        return a;
    }

    /**
     * ch
     * check if the use is in the system.
     * check if the use is Guest or connecting member
     *
     * @param uuid
     * @return boolean
     */
    public DResponseObj<Boolean> isOnline(UUID uuid) {
        logger.debug("UserManager isOnline");
        if (LoginUsers.containsKey(uuid) || GuestVisitors.containsKey(uuid)) {
            return new DResponseObj<>(true, -1);
        } else {
            return new DResponseObj<>(false, ErrorCode.NOTONLINE);
        }

    }


    public DResponseObj<ConcurrentHashMap<String, User>> getMembers() {
        logger.debug("UserManager getMembers");
        return new DResponseObj<>(members, -1);
    }

    public DResponseObj<Boolean> changePassword(UUID uuid, String email, String password, String newPassword) {
        if (!isOnline(uuid).value) {
            return new DResponseObj<>(ErrorCode.NOTONLINE);
        }
        if (!members.containsKey(email)) {
            return new DResponseObj<>(ErrorCode.NOTMEMBER);
        }
        DResponseObj<Boolean> res = Validator.isValidPassword(newPassword);
        if (res.value == false) {
            return new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
        }
        User u = members.get(email);
        res = u.isPasswordEquals(password);
        if (res.value == false) {
            return new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
        }
        res = u.changePassword(newPassword);
        if (res.errorOccurred()) {
            return new DResponseObj<>(res.errorMsg);
        }
        return new DResponseObj<>(true, -1);
    }


    public DResponseObj<ConcurrentHashMap<UUID, User>> getGuestVisitors() {
        logger.debug("UserManager getGuestVisitors");
        return new DResponseObj<>(GuestVisitors, -1);
    }


    public DResponseObj<ConcurrentHashMap<UUID, User>> getLoginUsers() {
        logger.debug("UserManager getLoginUsers");
        return new DResponseObj<>(LoginUsers, -1);
    }


    //UUID
    public DResponseObj<User> getLoggedUser(UUID uuid) {
        if (!LoginUsers.containsKey(uuid)) {
            return new DResponseObj<>(ErrorCode.NOTLOGGED);
        }
        return new DResponseObj<>(LoginUsers.get(uuid), -1);
    }


    public DResponseObj<User> getOnlineUser(UUID uuid) {
        if (GuestVisitors.containsKey(uuid)) {
            return new DResponseObj<>(GuestVisitors.get(uuid), -1);
        }
        if (LoginUsers.containsKey(uuid)) {
            return new DResponseObj<>(LoginUsers.get(uuid), -1);
        }
        return new DResponseObj<>(ErrorCode.NOTONLINE);
    }

    /**
     * get list of users to notify them of the message.
     * Note: if the users are not logged in
     * @param userList list of members in the market
     * @param msg the msg to notify them
     */
    public void notifyUsers(List<User> userList, String msg){
        List<UUID> loggedInUsers = new ArrayList<>();
        List<String> notLoggedInUsers = new ArrayList<>();
        userList.forEach(user -> {
            DResponseObj<UUID> usersIsLoggedIn = isLogged(user.getEmail().value);
            if (!usersIsLoggedIn.errorOccurred()) {
                loggedInUsers.add(usersIsLoggedIn.value);
            }
            else {
                notLoggedInUsers.add(user.getEmail().value);
            }
        });
        loggedInUsers.forEach(uuid -> {
            alertService.notifyUser(uuid,msg);
        });
        notLoggedInUsers.forEach(username->{
            alertService.notifyUser(username,msg);
        });
    }


    public  DResponseObj<ConcurrentHashMap<UUID, User>> getloggedInMembers(UUID uuid) {
        logger.debug("UserManager getloggedInMembers");
        DResponseObj<User> logIN = getLoggedUser(uuid);
        if (logIN.errorOccurred()) return new DResponseObj<>(null,logIN.errorMsg);
        DResponseObj<Boolean> result = PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.getAllLoggedInUsers, logIN.value, null);
        if (result.errorOccurred()) return new DResponseObj<>(null, result.errorMsg);
        return new DResponseObj<>(LoginUsers, -1);
    }

    public DResponseObj<ConcurrentHashMap<String, User>> getloggedOutMembers(UUID uuid) {
        logger.debug("UserManager getloggedOutMembers");
        DResponseObj<User> logIN = getLoggedUser(uuid);
        if (logIN.errorOccurred()) return new DResponseObj<>(null,logIN.errorMsg);
        DResponseObj<Boolean> result = PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.getAllLoggedOutUsers, logIN.value, null);
        if (result.errorOccurred()) return new DResponseObj<>(null, result.errorMsg);
        return new DResponseObj<>(members, -1);
    }
}
