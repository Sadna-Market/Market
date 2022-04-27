package Stabs;

import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.Market.userTypes;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.*;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class UserManagerStab extends UserManager {

    private StampedLock LockUsers= new StampedLock();

    /** menage all the members that have a saved user in the system , key : Emile , value : User*/
    ConcurrentHashMap<String, User> members;  // menage all the members that have a saved user in the system , key : Emile , value : User
    /** menage all the Visitors in the System , Visitors is set combines a members and gusts. key : userId , value Guest */
    ConcurrentHashMap<UUID, Guest> GuestVisitors ;
    /**menage all the logged in users in the system, key : user id  , value : User*/
    ConcurrentHashMap<UUID,User> LoginUsers;

    static Logger logger= Logger.getLogger(ShoppingBag.class);

    public UserManagerStab(){
        members = new ConcurrentHashMap<>();
        GuestVisitors = new ConcurrentHashMap<>();
        LoginUsers = new ConcurrentHashMap<>();
    }



    public UUID GuestVisit() {return UUID.randomUUID();

    }

    public boolean GuestLeave(UUID guestId) {
        return true;
    }



    public  boolean Login(UUID userID,String email, String password) {
        return true;
    }

    public boolean Logout(UUID userId) {
        return true;
    }


    public boolean AddNewMember(UUID uuid,String email, String Password,String phoneNumber,String CreditCared,String CreditDate) {
        return true;
    }



    public boolean isOwner(UUID user, Store store) {
        return true;
    }

    public boolean addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        return true;
    }

    public boolean addFounder(UUID userId, Store store) {
        return true;
    }
    public boolean addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        return true;
    }



    public boolean setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm) {
        return true;
    }

    public boolean getRolesInStore(int userId, Store store)
    {
        return true;
    }


    /**return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public boolean isLogged(UUID uuid){
        return true;
    }

    public ShoppingCart getUserShoppingCart(UUID userId)
    {
        return new ShoppingCart();
    }

    /** ch
     * check if the use is in the system.
     * check if the use is Guest or connecting member
     * @param uuid
     * @return boolean
     */
    public boolean isOnline(UUID uuid){
        return true;
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
}
