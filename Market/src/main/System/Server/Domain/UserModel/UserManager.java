
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



    private StampedLock LockUsers= new StampedLock();

    /** menage all the members that have a saved user in the system , key : Emile , value : User*/
    ConcurrentHashMap<String,User> members;  // menage all the members that have a saved user in the system , key : Emile , value : User
    /** menage all the Visitors in the System , Visitors is set combines a members and gusts. key : userId , value Guest */
    ConcurrentHashMap<UUID,User> GuestVisitors ;
    /**menage all the logged in users in the system, key : user id  , value : User*/
    ConcurrentHashMap<UUID,User> LoginUsers;

    static Logger logger= Logger.getLogger(ShoppingBag.class);

    public UserManager(){
        members = new ConcurrentHashMap<>();
        GuestVisitors = new ConcurrentHashMap<>();
        LoginUsers = new ConcurrentHashMap<>();
    }

    public DResponseObj<Boolean> isMember(String email){
        return new DResponseObj<>(members.containsKey(email));
    }

    public DResponseObj<UUID> GuestVisit(){
        logger.debug("UserManager GuestVisit");
        UUID newid = UUID.randomUUID();
        User guest = new User();
        GuestVisitors.put(newid, guest);
        return new DResponseObj<>(newid);

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
            UUID newMemberUUid= UUID.randomUUID();
            LoginUsers.put(newMemberUUid, LogUser);
            GuestVisitors.remove(userID);
            return new DResponseObj<>(newMemberUUid);
        } else {
            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOTVALIDINPUT);
            return a;
        }

    }

    public DResponseObj<Boolean> ishasSystemManager(){
        for(String mail : members.keySet()){
            if(PermissionManager.getInstance().isSystemManager(mail).value){
                return new DResponseObj<>(true);
            }
        }
        return new DResponseObj<>(false);
    }

    public DResponseObj<UUID> Logout(UUID userId) {
        if (LoginUsers.containsKey(userId)) {
            LoginUsers.remove(userId);
            User guest = new User();
            UUID newMemberUUid= UUID.randomUUID();
            GuestVisitors.put(newMemberUUid, guest);
            return new DResponseObj<>(newMemberUUid);
        }
        DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOTLOGGED);
        return a;
    }


    public DResponseObj<Boolean> AddNewMember(UUID uuid, String email, String Password, String phoneNumber) {

        long stamp= LockUsers.writeLock();
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
            DResponseObj <Boolean> res= Validator.isValidPassword(Password);
            if(res.value==false){
                return new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
            }
            res = Validator.isValidEmailAddress(email);
            if(res.value==false){
                return new DResponseObj<>(ErrorCode.NOT_VALID_EMILE);
            }
            res=Validator.isValidPhoneNumber(phoneNumber);
            if(res.value==false){
                return new DResponseObj<>(ErrorCode.NOT_VALID_PHONE);
            }

            User user = new User(email, Password,phoneNumber);
            members.put(email, user);
            return new DResponseObj<>(true);
        }finally {
            LockUsers.unlockWrite(stamp);
        }

    }

    public DResponseObj<Boolean> isFounder(Store store,String email) {
        if(!members.containsKey(email)){
            return new DResponseObj<Boolean>(false,ErrorCode.NOTMEMBER);
        }
        return PermissionManager.getInstance().isFounder(members.get(email),store);
    }




    public DResponseObj<Boolean> isOwner(UUID user,Store store) {
        if(!isLogged(user).value){
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
            return a;
        }
        User u = LoginUsers.get(user);
        logger.debug("UserManager isOwner");
        PermissionManager permissionManager =PermissionManager.getInstance();
        return new DResponseObj<>( permissionManager.getGranteeUserType(u,store).equals(userTypes.owner));
    }

    public DResponseObj<Boolean> addNewStoreOwner(UUID userId, Store store, String newOwnerEmail) {
        logger.debug("UserManager addNewStoreOwner");
        if(isLogged(userId).value ) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(userId,store).value) {
                User newOwner = members.get(newOwnerEmail);
                return loggedUser.addNewStoreOwner(newOwner,store);
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }

    public DResponseObj<Boolean> isCartExist(UUID uuid){
        if(GuestVisitors.containsKey(uuid)){
            return new DResponseObj<>(true);
        }
        if(LoginUsers.containsKey(uuid)){
            return new DResponseObj<>(true);
        }
        return new DResponseObj<>(false);
    }

    public DResponseObj<Boolean> addFounder(UUID userId, Store store) {
        logger.debug("UserManager addFounder");

        if(isLogged(userId).value) {
            User user = LoginUsers.get(userId);
            return user.addFounder(store);
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }
    public DResponseObj<Boolean> addNewStoreManager(UUID uuid, Store store, String newMangerEmail) {
        logger.debug("UserManager addNewStoreManager");

        if(isLogged(uuid).value) {
            User loggedUser = LoginUsers.get(uuid);
            if (isOwner(uuid,store).value) {
                User newManager = members.get(newMangerEmail);
                return loggedUser.addNewStoreManager(newManager,store);
            }}
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }



    public DResponseObj<Boolean> setManagerPermissions(UUID userId, Store store, String email, permissionType.permissionEnum perm ,boolean onof) {
        logger.debug("UserManager setManagerPermissions");
        if(isLogged(userId).value ) {
            User loggedUser = LoginUsers.get(userId);
            if (isOwner(userId,store).value) {
                User Manager = members.get(email);
                return loggedUser.setManagerPermissions(Manager,store,perm,onof);
            }
        }
        DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
        a.errorMsg = ErrorCode.NOTLOGGED;
        return a;
    }



    /**return true if existing member is logged in the system
     *
     * @param uuid
     * @return boolean
     */
    public DResponseObj<Boolean> isLogged(UUID uuid){
        logger.debug("UserManager isLogged");
        if(LoginUsers.containsKey(uuid)){
            return new DResponseObj<>(true);
        }
        else {
            return new DResponseObj<>( false);
        }
    }

    public DResponseObj<ShoppingCart> getUserShoppingCart(UUID userId)
    {
        logger.debug("UserManager getUserShoppingCart");

        if(GuestVisitors.containsKey(userId)){
            return new DResponseObj<ShoppingCart>( GuestVisitors.get(userId).GetSShoppingCart().value);
        }
        else if(members.containsKey(userId)){
            return new DResponseObj<ShoppingCart>( members.get(userId).GetSShoppingCart().value);
        }
        DResponseObj<ShoppingCart> a = new DResponseObj<>();
        a.errorMsg = ErrorCode.NOTONLINE;
        return a;
    }

    /** ch
     * check if the use is in the system.
     * check if the use is Guest or connecting member
     * @param uuid
     * @return boolean
     */
    public DResponseObj<Boolean>  isOnline(UUID uuid){
        logger.debug("UserManager isOnline");
        if (LoginUsers.containsKey(uuid) || GuestVisitors.containsKey(uuid)) {
            return new DResponseObj<>(true);
        } else {
            return new DResponseObj<>( false);
        }

    }


    public DResponseObj< ConcurrentHashMap<String, User>> getMembers() {
        logger.debug("UserManager getMembers");
        return new DResponseObj<>( members);
    }

    public DResponseObj<Boolean> changePassword(UUID uuid, String email , String password,String newPassword)
    {
        if(!isOnline(uuid).value)
        {
            return new DResponseObj<>(ErrorCode.NOTONLINE);
        }
        if(!members.containsKey(email))
        {
            return new DResponseObj<>(ErrorCode.NOTMEMBER);
        }
        DResponseObj<Boolean> res = Validator.isValidPassword(newPassword);
        if(res.value==false){
            return new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
        }
        User u = members.get(uuid);
        res = u.isPasswordEquals(password);
        if(res.value==false){
            return new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
        }
        res =u.changePassword(newPassword);
        if(res.errorOccurred()){
            return res;
        }
        return new DResponseObj<>(true);
    }


    public DResponseObj< ConcurrentHashMap<UUID, User>> getGuestVisitors() {
        logger.debug("UserManager getGuestVisitors");
        return new DResponseObj<>( GuestVisitors);
    }


    public DResponseObj<ConcurrentHashMap<UUID, User>> getLoginUsers() {
        logger.debug("UserManager getLoginUsers");
        return new DResponseObj<>( LoginUsers);
    }


    //UUID
    public DResponseObj< User> getOnlineUser(UUID uuid){
        return new DResponseObj<>( LoginUsers.getOrDefault(uuid, null));
    }


}
