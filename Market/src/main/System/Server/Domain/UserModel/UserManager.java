package main.System.Server.Domain.UserModel;

import main.ErrorCode;
import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.Market.userTypes;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;
//*

/**
 userManger mange all users in the system its saved all the users that sign in to the system ,users that logged and all current guests
 then someone enter to the system the system will generate an a unique userID,
 the user id will not saved in the db
 the user id is used only for manging the current online visitor : Guests , Members
 */



public class UserManager {
    int notLoged = 10;
    int notonline = 11;
    int notvalidInput =12;
    int notMember =13;


    private StampedLock LockUsers= new StampedLock();

    /** menage all the members that have a saved user in the system , key : Emile , value : User*/
    ConcurrentHashMap<String,User> members;  // menage all the members that have a saved user in the system , key : Emile , value : User
    /** menage all the Visitors in the System , Visitors is set combines a members and gusts. key : userId , value Guest */
    ConcurrentHashMap<UUID,Guest> GuestVisitors ;
    /**menage all the logged in users in the system, key : user id  , value : User*/
    ConcurrentHashMap<UUID,User> LoginUsers;

    static Logger logger= Logger.getLogger(ShoppingBag.class);

    public UserManager(){
        members = new ConcurrentHashMap<>();
        GuestVisitors = new ConcurrentHashMap<>();
        LoginUsers = new ConcurrentHashMap<>();
    }



    public DResponseObj<UUID> GuestVisit(){
        logger.debug("UserManager GuestVisit");
        UUID newid = UUID.randomUUID();
        Guest guest = new Guest();
        GuestVisitors.put(newid, guest);
        return new DResponseObj<>(newid);

    }

    public DResponseObj<Boolean> GuestLeave(UUID guestId) {
        logger.debug("UserManager GuestLeave");
        if (!GuestVisitors.containsKey(guestId)) {
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
            a.errorMsg = ""+ ErrorCode.NOTONLINE;
            return a;
        } else {
            GuestVisitors.remove(guestId);
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(true);

            return a;
        }
    }



    public DResponseObj<Boolean> Login(UUID userID, String email, String password) {
        logger.debug("UserManager Login");
        if (GuestVisitors.containsKey(userID) && members.containsKey(email) && !LoginUsers.containsKey(userID) && members.get(email).isPasswordEquals(password)) {
            User LogUser = members.get(email);
            LoginUsers.put(userID, LogUser);
            GuestVisitors.remove(userID);
            return new DResponseObj<>(true);
        } else {
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
            return a;
        }

    }

    public DResponseObj<Boolean> Logout(UUID userId) {
        if (LoginUsers.containsKey(userId)) {
            LoginUsers.remove(userId);
            Guest guest = new Guest();
            GuestVisitors.put(userId, guest);
            return new DResponseObj<>(true);
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        return a;
    }


    public DResponseObj<Boolean> AddNewMember(UUID uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {

        long stamp= LockUsers.writeLock();
        try {
            if (!isOnline(uuid)) {
                DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
                a.errorMsg = ""+notLoged;
                return a;
            }
            if (members.containsKey(email)) {

                DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
                a.errorMsg = ""+notMember;
                return a;            }
            if (!Validator.isValidEmailAddress(email) || !Validator.isValidPassword(Password) || !Validator.isValidPhoneNumber(phoneNumber)||
                    !Validator.isValidCreditCard(CreditCared)||!Validator.isValidCreditDate(CreditDate)){

                DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
                a.errorMsg = ""+notvalidInput;
                return a;
            }
            User user = new User(email, Password,phoneNumber,CreditCared,CreditDate);
            members.put(email, user);
            return new DResponseObj<>(true);
        }finally {
            LockUsers.unlockWrite(stamp);
        }

    }



    public boolean isOwner(UUID user,Store store) {
        if(!isLogged(user)){

            return false;
        }
        User u = LoginUsers.get(user);
        logger.debug("UserManager isOwner");
        PermissionManager permissionManager =PermissionManager.getInstance();
        return permissionManager.getGranteeUserType(u,store).equals(userTypes.owner);

    }

    public DResponseObj<Boolean> addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        logger.debug("UserManager addNewStoreOwner");
        if(isLogged(userId) ) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(userId,store)) {
                User newOwner = members.get(newOwnerEmail);
                return loggedUser.addNewStoreOwner(newOwner,store);
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ""+notLoged;
        return a;
    }

    public DResponseObj<Boolean> addFounder(UUID userId, Store store) {
        logger.debug("UserManager addFounder");

        if(isLogged(userId)) {
            User user = LoginUsers.get(userId);
            return user.addFounder(store);
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ""+notLoged;
        return a;
    }
    public DResponseObj<Boolean> addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        logger.debug("UserManager addNewStoreManager");

        if(isLogged(uuid) ) {
            User loggedUser = LoginUsers.get(uuid);
            if (isOwner(uuid,store)) {
                User newManager = members.get(newMangerEmail);
                return loggedUser.addNewStoreManager(newManager,store);
            }}
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ""+notLoged;
        return a;
    }



    public DResponseObj<Boolean> setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm) {
        logger.debug("UserManager setManagerPermissions");
        if(isLogged(userId) ) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(userId,store)) {
                User Manager = members.get(email);
                return loggedUser.setManagerPermissions(Manager,store,perm);
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ""+notLoged;
        return a;
    }

    public boolean getRolesInStore(int userId, Store store)
    {
        logger.debug("UserManager getRolesInStore");
        return members.get(userId).getRolesInStore(store);
    }


    /**return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public boolean isLogged(UUID uuid){
        logger.debug("UserManager isLogged");
        if(LoginUsers.containsKey(uuid)){
            return true;
        }
        else {
            return false;
        }
    }

    public DResponseObj<ShoppingCart> getUserShoppingCart(UUID userId)
    {
        logger.debug("UserManager getUserShoppingCart");

        if(GuestVisitors.containsKey(userId)){
            return new DResponseObj<>( GuestVisitors.get(userId).getShoppingCart());
        }
        else if(members.containsKey(userId)){
            return new DResponseObj<>( members.get(userId).getShoppingCart());
        }
        else return null;
    }

    /** ch
     * check if the use is in the system.
     * check if the use is Guest or connecting member
     * @param uuid
     * @return boolean
     */
    public boolean isOnline(UUID uuid){
        logger.debug("UserManager isOnline");
        if (LoginUsers.containsKey(uuid) || GuestVisitors.containsKey(uuid)) {
            return true;
        } else {
            return false;
        }

    }


    public ConcurrentHashMap<String, User> getMembers() {
        logger.debug("UserManager getMembers");

        return members;
    }


    public ConcurrentHashMap<UUID, Guest> getGuestVisitors() {
        logger.debug("UserManager getGuestVisitors");
        return GuestVisitors;
    }


    public ConcurrentHashMap<UUID, User> getLoginUsers() {
        logger.debug("UserManager getLoginUsers");
        return LoginUsers;
    }


    //UUID
    public User getOnlineUser(UUID uuid){
        return LoginUsers.getOrDefault(uuid, null);
    }

}
