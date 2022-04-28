package Stabs;

import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.*;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
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



    public ATResponseObj<UUID> GuestVisit() {return new ATResponseObj<>(UUID.randomUUID());

    }

    public ATResponseObj<Boolean> GuestLeave(UUID guestId) {
        return new ATResponseObj<>(true);
    }


    public ATResponseObj<Boolean> Login(UUID userID, String email, String password) {
        
        return new ATResponseObj<>(true);
    }

    public ATResponseObj<Boolean> Logout(UUID userId) {
        
        return new ATResponseObj<>(true);
    }


    public ATResponseObj<Boolean> AddNewMember(UUID uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {
        return new ATResponseObj<>(true);
    }



    public ATResponseObj<Boolean> isOwner(UUID user, Store store) {
        return new ATResponseObj<>(true);
    }

    public ATResponseObj<Boolean> addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        return new ATResponseObj<>(true);
    }

    public ATResponseObj<Boolean> addFounder(UUID userId, Store store) {
        return new ATResponseObj<>(true);
    }
    public ATResponseObj<Boolean> addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        return new ATResponseObj<>(true);
    }



    public ATResponseObj<Boolean> setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm) {
        return new ATResponseObj<>(true);
    }

    public ATResponseObj<Boolean> getRolesInStore(int userId, Store store)
    {
        return new ATResponseObj<>(true);
    }


    /**return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public ATResponseObj<Boolean> isLogged(UUID uuid){
        return new ATResponseObj<>(true);
    }

    public ATResponseObj<ShoppingCart> getUserShoppingCart(UUID userId)
    {
        return new ATResponseObj<>(new ShoppingCart());
    }

    /** ch
     * check if the use is in the system.
     * check if the use is Guest or connecting member
     * @param uuid
     * @return boolean
     */
    public ATResponseObj<Boolean> isOnline(UUID uuid){
        return new ATResponseObj<>(true);
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
