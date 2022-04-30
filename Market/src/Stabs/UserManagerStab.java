package Stabs;

import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.*;
import main.System.Server.Domain.Response.DResponseObj;
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



    public DResponseObj<UUID> GuestVisit() {return new DResponseObj<>(UUID.randomUUID());

    }

    public DResponseObj<Boolean> GuestLeave(UUID guestId) {
        return new DResponseObj<>(true);
    }


    public DResponseObj<UUID> Login(UUID userID, String email, String password) {
        
        return new DResponseObj<>(UUID.randomUUID());
    }

    public DResponseObj<UUID> Logout(UUID userId) {
        
        return new DResponseObj<>(UUID.randomUUID());
    }


    public DResponseObj<Boolean> AddNewMember(UUID uuid, String email, String Password, String phoneNumber, String CreditCared, String CreditDate) {
        return new DResponseObj<>(true);
    }



    public DResponseObj<Boolean> isOwner(UUID user, Store store) {
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> addFounder(UUID userId, Store store) {
        return new DResponseObj<>(true);
    }
    public DResponseObj<Boolean> addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        return new DResponseObj<>(true);
    }



    public DResponseObj<Boolean> setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm) {
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> getRolesInStore(int userId, Store store)
    {
        return new DResponseObj<>(true);
    }


    /**return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public DResponseObj<Boolean> isLogged(UUID uuid){
        return new DResponseObj<>(true);
    }

    public DResponseObj<ShoppingCart> getUserShoppingCart(UUID userId)
    {
        return new DResponseObj<>(new ShoppingCart());
    }

    /** ch
     * check if the use is in the system.
     * check if the use is Guest or connecting member
     * @param uuid
     * @return boolean
     */
    public DResponseObj<Boolean> isOnline(UUID uuid){
        return new DResponseObj<>(true);
    }


    public DResponseObj<ConcurrentHashMap<String, User>> getMembers() {
        logger.debug("UserManager getMembers");

        return new DResponseObj<>(members);
    }


    public DResponseObj<ConcurrentHashMap<UUID, Guest>> getGuestVisitors() {
        logger.debug("UserManager getGuestVisitors");
        return new DResponseObj<>(GuestVisitors);
    }


    public DResponseObj<ConcurrentHashMap<UUID, User>> getLoginUsers() {
        logger.debug("UserManager getLoginUsers");
        return new DResponseObj<>(LoginUsers);
    }
}
