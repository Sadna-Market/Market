package com.example.demo.Domain.UserModel;

import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.DataAccess.Services.UserService;
import com.example.demo.Domain.AlertService.AlertServiceDemo;
import com.example.demo.Domain.AlertService.IAlertService;
import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Market.*;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.History;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Domain.StoreModel.Store;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
//*

/**
 * userManger mange all users in the system its saved all the users that sign in to the system ,users that logged and all current guests
 * then someone enter to the system the system will generate an a unique userID,
 * the user id will not saved in the db
 * the user id is used only for manging the current online visitor : Guests , Members
 */

@Component
public class UserManager {

    private IAlertService alertService;

    @Autowired
    private UserService dataUserService;

    private StampedLock LockUsers = new StampedLock();

    /**
     * menage all the members that have a saved user in the system , key : Emile , value : User
     */
    ConcurrentHashMap<String, User> members; // menage all the members that have a saved user in the system , key : Emile , value : User
    /**
     * menage all the Visitors in the System , Visitors is set combines a members and gusts. key : userId , value Guest
     */
    ConcurrentHashMap<UUID, User> GuestVisitors;
    /**
     * menage all the logged in users in the system, key : user id  , value : User
     */
    ConcurrentHashMap<UUID, User> LoginUsers;

    static Logger logger = Logger.getLogger(UserManager.class);

    @Autowired
    public UserManager(IAlertService alertService) { //For real application dependency injection
        this.alertService = alertService;
        this.members = new ConcurrentHashMap<>();
        this.GuestVisitors = new ConcurrentHashMap<>();
        this.LoginUsers = new ConcurrentHashMap<>();
    }


    public UserManager() { //For AT only.
        this.alertService = new AlertServiceDemo();
        this.members = new ConcurrentHashMap<>();
        this.GuestVisitors = new ConcurrentHashMap<>();
        this.LoginUsers = new ConcurrentHashMap<>();
    }

    public DResponseObj<Boolean> isMember(String email) {
        return members.containsKey(email) ? new DResponseObj<>(true, -1) : new DResponseObj<>(false, ErrorCode.NOTMEMBER);
    }

    public DResponseObj<List<HashMap<String, Object>>> getAllusers() {
        List<HashMap<String, Object>> res = new LinkedList<>();
        for (User mem : members.values()) {
            if (isLogged(mem)) {
                HashMap<String, Object> h = new HashMap<>();
                h.put("email", mem.email);
                h.put("isLogged", true);
                res.add(h);
            } else {
                HashMap<String, Object> h = new HashMap<>();
                h.put("email", mem.email);
                h.put("isLogged", false);
                res.add(h);
            }
        }
        return new DResponseObj<>(res);
    }


    public Boolean isLogged(User user) {
        return LoginUsers.values().contains(user);
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
            logger.debug("not a member, email:" + email);
            return new DResponseObj<>(ErrorCode.NOTMEMBER);
        }
        User u = members.get(email);
        logger.debug("UserManager isOwner email: " + email + " store:" + store);
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<userTypes> res = permissionManager.getGranteeUserType(u, store);
        return res.value.equals(userTypes.owner) ? new DResponseObj<>(true, -1) : new DResponseObj<>(false, ErrorCode.NOTOWNER);

    }

    public DResponseObj<Boolean> isManager(String email, Store store) {
        if (!members.containsKey(email)) {
            logger.debug("not a member, email:" + email);
            return new DResponseObj<>(ErrorCode.NOTMEMBER);
        }
        User u = members.get(email);
        logger.debug("UserManager isManager email: " + email + " store:" + store);
        PermissionManager permissionManager = PermissionManager.getInstance();
        DResponseObj<userTypes> res = permissionManager.getGranteeUserType(u, store);
        return res.value.equals(userTypes.manager) ? new DResponseObj<>(true, -1) : new DResponseObj<>(false, ErrorCode.NOT_MANAGER);

    }

    public DResponseObj<Boolean> GuestLeave(UUID guestId) {
        logger.debug("UserManager GuestLeave");
        if (!GuestVisitors.containsKey(guestId)) {
            logger.debug("uuid is not in the guests");
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(false);
            a.errorMsg = ErrorCode.NOTONLINE;
            return a;
        } else {
            logger.debug("usermenager GuestLeave");
            GuestVisitors.remove(guestId);
            DResponseObj<Boolean> a = new DResponseObj<Boolean>(true);

            return a;
        }
    }


    public DResponseObj<UUID> Login(UUID userID, String email, String password) {
        logger.debug("UserManager Login email: " + email);
        if (!GuestVisitors.containsKey(userID)) {
            logger.debug("UserManager Login email: " + email + " NOT online");
            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOTONLINE);
            return a;
        }
        if (!members.containsKey(email)) {
            logger.debug("UserManager Login email: " + email + " not a member");
            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOTMEMBER);
            return a;
        }
        if (LoginUsers.containsKey(userID)) {
            logger.debug("UserManager Login email: " + email + "aallready logged");
            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.ALLRADYLOGED);
            return a;
        }
        if (isEmailLoged(email)) {
            logger.debug("UserManager Login email: " + email + "aallready logged");
            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.ALLRADYLOGED);
            return a;
        }
        if (members.get(email).isPasswordEquals(password).value) {
            User LogUser = members.get(email);
            UUID newMemberUUid = UUID.randomUUID();
            LoginUsers.put(newMemberUUid, LogUser);
            GuestVisitors.remove(userID);
            alertService.modifyDelayIfExist(LogUser.email, newMemberUUid);
            return new DResponseObj<>(newMemberUUid);
        } else {
            logger.debug("Login email: " + email + " the password is not correct");

            DResponseObj<UUID> a = new DResponseObj<>(ErrorCode.NOT_VALID_PASSWORD);
            return a;
        }

    }

    public boolean isEmailLoged(String email) {
        for (User u : LoginUsers.values()) {
            if (u.email.equals(email)) {
                return true;
            }
        }
        return false;
    }

    public DResponseObj<Boolean> resetShoppingCart(UUID uuid) {
        DResponseObj<User> u = getOnlineUser(uuid);
        if (u.errorOccurred()) return new DResponseObj<>(false, u.errorMsg);
        return u.value.resetCart();
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
            //db
            if (dataUserService != null) { //because no autowire in AT
                DataUser dataUser = user.getDataObject();
                if (!dataUserService.insertUser(dataUser)) {
                    logger.error(String.format("didnt save user %s", user.email));
                    return new DResponseObj<>(false, ErrorCode.DB_ERROR);
                }
            }
            //
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

    public DResponseObj<List<String>> getAllMembers(UUID userId) {
        DResponseObj<Boolean> res = isLogged(userId);
        if (res.errorOccurred() || !res.value) return new DResponseObj<>(ErrorCode.NOTLOGGED);
        if (!LoginUsers.containsKey(userId)) return new DResponseObj<>(ErrorCode.NOTMEMBER);
        User admin = LoginUsers.get(userId);
        DResponseObj<Boolean> isadminres = PermissionManager.getInstance().isSystemManager(admin.email);
        if (isadminres.errorOccurred() || !isadminres.value) return new DResponseObj<>(ErrorCode.NOTADMIN);
        List<String> em = new LinkedList<>();
        for (User u : members.values()) {
            em.add(u.email);
        }
        return new DResponseObj<>(em, -1);
    }

    public DResponseObj<Boolean> removeMember(UUID userId, String email) {
        DResponseObj<Boolean> res = isLogged(userId);
        if (res.errorOccurred() || !res.value) return new DResponseObj<>(false, ErrorCode.NOTLOGGED);
        if (!LoginUsers.containsKey(userId)) return new DResponseObj<>(false, ErrorCode.NOTMEMBER);
        User admin = LoginUsers.get(userId);
        DResponseObj<Boolean> isadminres = PermissionManager.getInstance().isSystemManager(admin.email);
        if (isadminres.errorOccurred() || !isadminres.value) return new DResponseObj<>(false, ErrorCode.NOTADMIN);

        if (!members.containsKey(email)) return new DResponseObj<>(false, ErrorCode.NOTMEMBER);
        if (admin.email.equals(email)) return new DResponseObj<>(false, ErrorCode.NOTVALIDINPUT);
        members.remove(email);
        //db
        if (dataUserService != null) {
            if (!dataUserService.deleteUser(email)) {
                logger.error(String.format("failed to remove user %s", email));
                return new DResponseObj<>(false, ErrorCode.DB_ERROR);
            }
        }
        return new DResponseObj<>(true, -1);
    }

    public DResponseObj<Boolean> removeStoreOwner(UUID userId, Store store, String ownerEmail) {
        logger.debug("UserManager removeStoreOwner");
        if (isLogged(userId).errorOccurred()) return new DResponseObj<>(false, ErrorCode.NOTLOGGED);
        User loggedUser = LoginUsers.get(userId);
        if (!isOwner(userId, store).value) return new DResponseObj<>(false, ErrorCode.NOTOWNER);
        User ownerToRemove = members.get(ownerEmail);
        DResponseObj<Boolean> removePermission = PermissionManager.getInstance().removeOwnerPermissionCompletely(ownerToRemove, store, loggedUser);
        if (!removePermission.errorOccurred()) {
            //change all permission grantor to the founder (because we delete the grantor user)
            String founder = store.getFounder().value;
            User founderUser = getMember(founder).value;
            for (Permission permission : ownerToRemove.getGrantorPermission().value) {
                if (permission.getStore().value.getStoreId().value.equals(store.getStoreId().value)) {
                    permission.setGrantor(founderUser);
                    founderUser.addGrantorPermission(permission);
                    ownerToRemove.removeGrantorPermission(permission);
                }
            }
        }
        return removePermission;
    }


    public DResponseObj<Boolean> removeStoreManager(UUID userId, Store store, String MenagerEmail) {
        logger.debug("UserManager removeStoreManager");
        if (isLogged(userId).errorOccurred()) return new DResponseObj<>(false, ErrorCode.NOTLOGGED);
        User loggedUser = LoginUsers.get(userId);
        if (!isOwner(userId, store).value) return new DResponseObj<>(false, ErrorCode.NOTOWNER);
        User MenagerToRemove = members.get(MenagerEmail);
        DResponseObj<Boolean> removePermission = PermissionManager.getInstance().removeManagerPermissionCompletely(MenagerToRemove, store, loggedUser);
        return removePermission;
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
                DResponseObj<Boolean> res = loggedUser.setManagerPermissions(Manager, store, perm, onof);
                if (res.errorOccurred()) return res;
                logger.info(String.format("notifying %s for his permission setup", Manager.email));
                String msg = String.format("Your permission %s was %s store %d by %s", perm.toString(),
                        onof ? "added to" : "removed from", store.getStoreId().value, loggedUser.email);
                notifyUsers(List.of(Manager), msg);
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
     *
     * @param username
     * @return boolean
     */
    public DResponseObj<UUID> isLogged(String username) {
        logger.debug("UserManager isLogged");
        for (Map.Entry<UUID, User> entry : LoginUsers.entrySet()) {
            if (entry.getValue().email.equals(username))
                return new DResponseObj<>(entry.getKey(), -1);
        }
        return new DResponseObj<>(null, ErrorCode.NOTLOGGED);
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
        //db
        if (dataUserService != null) {
            var dataUser = u.getDataObject();
            if (!dataUserService.updateUser(dataUser)) {
                logger.error("couldn't change password in db");
                return new DResponseObj<>(false, ErrorCode.DB_ERROR);
            }
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

    public DResponseObj<User> getMember(String email) {
        if (members.containsKey(email)) {
            logger.info(String.format("member %s is present, returning the member", email));
            return new DResponseObj<>(members.get(email), -1);
        }
        logger.warn(String.format("member %s not found in members", email));
        return new DResponseObj<>(null, ErrorCode.NOTMEMBER);
    }

    /**
     * get list of users to notify them of the message.
     * Note: if the users are not logged in
     *
     * @param userList list of members in the market
     * @param msg      the msg to notify them
     */
    public void notifyUsers(List<User> userList, String msg) {
        List<UUID> loggedInUsers = new ArrayList<>();
        List<String> notLoggedInUsers = new ArrayList<>();
        userList.forEach(user -> {
            DResponseObj<UUID> usersIsLoggedIn = isLogged(user.getEmail().value);
            if (!usersIsLoggedIn.errorOccurred()) {
                loggedInUsers.add(usersIsLoggedIn.value);
            } else {
                notLoggedInUsers.add(user.getEmail().value);
            }
        });
        loggedInUsers.forEach(uuid -> {
            alertService.notifyUser(uuid, msg);
        });
        notLoggedInUsers.forEach(username -> {
            alertService.notifyUser(username, msg);
        });
    }


    public DResponseObj<ConcurrentHashMap<UUID, User>> getloggedInMembers(UUID uuid) {
        logger.debug("UserManager getloggedInMembers");
        DResponseObj<User> logIN = getLoggedUser(uuid);
        if (logIN.errorOccurred()) return new DResponseObj<>(null, logIN.errorMsg);
        DResponseObj<Boolean> result = PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.getAllLoggedInUsers, logIN.value, null);
        if (result.errorOccurred()) return new DResponseObj<>(null, result.errorMsg);
        return new DResponseObj<>(LoginUsers, -1);
    }

    public DResponseObj<ConcurrentHashMap<String, User>> getloggedOutMembers(UUID uuid) {
        logger.debug("UserManager getloggedOutMembers");
        DResponseObj<User> logIN = getLoggedUser(uuid);
        if (logIN.errorOccurred()) return new DResponseObj<>(null, logIN.errorMsg);
        DResponseObj<Boolean> result = PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.getAllLoggedOutUsers, logIN.value, null);
        if (result.errorOccurred()) return new DResponseObj<>(null, result.errorMsg);
        return new DResponseObj<>(members, -1);
    }

    /**
     * gets all stores that the user is the founder of this store.
     * clears all users permissions
     *
     * @param uuid
     * @param cancelMemberUsername
     * @return list of the stores that the user was the founder to cancel
     */
    public DResponseObj<List<Store>> cancelMembership(UUID uuid, String cancelMemberUsername) {
        //check if the username is a member
        DResponseObj<Boolean> isMember = isMember(cancelMemberUsername);
        if (isMember.errorOccurred()) return new DResponseObj<>(null, isMember.errorMsg);
        //get the user object
        DResponseObj<User> getUser = getLoggedUser(uuid);
        if (getUser.errorOccurred()) return new DResponseObj<>(null, getUser.errorMsg);
        User sysManager = getUser.value;
        //check if uuid (System manager) permission to cancel membership
        DResponseObj<Boolean> hasPermission = PermissionManager.getInstance().hasPermission(permissionType.permissionEnum.cancelMembership, sysManager, null);
        if (hasPermission.errorOccurred()) return new DResponseObj<>(null, hasPermission.errorMsg);

        DResponseObj<User> getCancel = getMember(cancelMemberUsername);
        if (getCancel.errorOccurred()) return new DResponseObj<>(null, getCancel.errorMsg);

        User toCancelUser = getCancel.value;
        DResponseObj<List<Store>> listOfStoreToDelete = PermissionManager.getInstance().removeAllPermissions(toCancelUser);
        if (listOfStoreToDelete.errorOccurred()) return listOfStoreToDelete;

        //change all permission grantor to the founder (because we delete the grantor user)
        for (Permission permission : toCancelUser.getGrantorPermission().value) {
            Store store = permission.getStore().value;
            String founder = store.getFounder().value;
            if (!founder.equals(toCancelUser.email)) {
                User founderUser = getMember(founder).value;
                permission.setGrantor(founderUser);
                founderUser.addGrantorPermission(permission);
            }
        }
        //remove instance of member for good
        deleteMemberPermanently(toCancelUser);
        return listOfStoreToDelete;
    }

    private void deleteMemberPermanently(User toCancelUser) {
        logger.info(String.format("deleting %s from market permanently", toCancelUser.email));
        User s = members.remove(toCancelUser.email);
        if (s == null)
            logger.warn(String.format("failed to delete. %s is not a member already", toCancelUser.email));
        if (dataUserService != null) {
            if (!dataUserService.deleteUser(toCancelUser.email)) {
                logger.error(String.format("failed to delete from db. %s is not a member already", toCancelUser.email));
            }
        }
    }
    @PostConstruct
    public void load() {
        List<DataUser> dataUsers = dataUserService.getAllUsers();
        dataUsers.forEach(duser -> {
            var decrypted = Validator.getInstance().decryptAES(duser.getPassword());
            User user = new User(duser.getUsername(),decrypted,duser.getPhoneNumber(),duser.getDateOfBirth());
            user.setHistories(
                    duser.getHistories().stream()
                            .map(dhistory -> new History(dhistory.getHistoryId(),
                                    dhistory.getSupplyId(),
                                    dhistory.getFinalPrice(),
                                    dhistory.getProducts().stream().map( dataProductStoreHistory->{
                                        var dataProductType = dataProductStoreHistory.getProductType();
                                        ProductType productType = new ProductType(dataProductType.getProductTypeId(),
                                                dataProductType.getProductName(),
                                                dataProductType.getDescription(),
                                                dataProductType.getCategory());
                                        return new ProductStore(productType,
                                                dataProductStoreHistory.getQuantity(),
                                                dataProductStoreHistory.getPrice());
                                    }).collect(Collectors.toList()),
                                    duser.getUsername())).collect(Collectors.toList())
            );
            members.put(user.email,user);
        });
    }
    //for db test
    public void deleteAllMembers(){
        members.clear();
    }

}
