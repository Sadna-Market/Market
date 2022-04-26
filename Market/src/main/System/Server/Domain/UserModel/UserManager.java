package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.Market.userTypes;
import main.System.Server.Domain.StoreModel.Store;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

/**
userManger mange all users in the system its saved all the users that sign in to the system ,users that logged and all current guests
then someone enter to the system the system will generate an a unique userID,
the user id will not saved in the db
the user id is used only for manging the current online visitor : Guests , Members
*/

public class UserManager {
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



    public UUID GuestVisit() {
            logger.debug("UserManager GuestVisit");
            UUID newid = UUID.randomUUID();
            Guest guest = new Guest();
            GuestVisitors.put(newid, guest);
            return newid;

    }

    public boolean GuestLeave(UUID guestId) {
        logger.debug("UserManager GuestLeave");
        if (!GuestVisitors.containsKey(guestId)) {
            return false;
        } else {
            GuestVisitors.remove(guestId);
            return true;
        }    }



    public  boolean Login(UUID userID,String email, String password) {
        logger.debug("UserManager Login");
        if (GuestVisitors.containsKey(userID) && members.containsKey(email) && !LoginUsers.containsKey(userID) && members.get(email).isPasswordEquals(password)) {
            User LogUser = members.get(email);
            LoginUsers.put(userID, LogUser);
            GuestVisitors.remove(userID);
            return true;
        } else {
            return false;
        }
    }

    public boolean Logout(UUID userId) {
        if (LoginUsers.containsKey(userId)) {
            LoginUsers.remove(userId);
            Guest guest = new Guest();
            GuestVisitors.put(userId, guest);
            return true;
        }
        return false;

    }


    public boolean AddNewMember(UUID uuid,String email, String Password,String phoneNumber,String CreditCared,String CreditDate) {

        long stamp= LockUsers.writeLock();
        try {
            logger.debug("UserManager AddNewMember");
            if (!isOnline(uuid)) {
                return false;
            }
            if (members.containsKey(email)) {
                System.out.println("lllllllllllllllllllll");

                return false;
            }
            if (!Validator.isValidEmailAddress(email) || !Validator.isValidPassword(Password) || !Validator.isValidPhoneNumber(phoneNumber)||
                    !Validator.isValidCreditCard(CreditCared)||!Validator.isValidCreditDate(CreditDate)){
                System.out.println("ppppppppppllllll");

                return false;
            }
            User user = new User(email, Password,phoneNumber,CreditCared,CreditDate);
            System.out.println("lllllllllllllllllllll");
            members.put(email, user);
            return true;
        }finally {
            LockUsers.unlockWrite(stamp);
        }

    }



    public boolean isOwner(User user,Store store) {
        return false;

    }

    public boolean addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        logger.debug("UserManager addNewStoreOwner");
        if(isLogged(userId) ) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(loggedUser,store)) {
                User newOwner = members.get(newOwnerEmail);
                return loggedUser.addNewStoreOwner(newOwner,store);
            }
        }
        return false;
    }

    public boolean addFounder(UUID userId, Store store) {
        return false;

    }
    public boolean addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        logger.debug("UserManager addNewStoreManager");

        if(isLogged(uuid) ) {
            User loggedUser = LoginUsers.get(uuid);
            if (isOwner(loggedUser,store)) {
                User newManager = members.get(newMangerEmail);
                return loggedUser.addNewStoreManager(newManager,store);
            }}
        return false;
    }



    public boolean setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm) {
        logger.debug("UserManager setManagerPermissions");
        if(isLogged(userId) ) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(loggedUser,store)) {
                User Manager = members.get(email);
                return loggedUser.setManagerPermissions(Manager,store,perm);
            }
        }
        return false;
    }

    public boolean getRolesInStore(int userId, Store store)
    {
        return false;

    }


    /**return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public boolean isLogged(UUID uuid){
        return false;


    }

    public ShoppingCart getUserShoppingCart(UUID userId)
    {
        logger.debug("UserManager getUserShoppingCart");

        if(GuestVisitors.containsKey(userId)){
            return GuestVisitors.get(userId).getShoppingCart();
        }
        else if(members.containsKey(userId)){
            return members.get(userId).getShoppingCart();
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
        return false;


    }


    public ConcurrentHashMap<String, User> getMembers() {
        return null;

    }


    public ConcurrentHashMap<UUID, Guest> getGuestVisitors() {
        return null;

    }


    public ConcurrentHashMap<UUID, User> getLoginUsers() {
       return null;
    }

}
