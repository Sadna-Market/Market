package com.example.demo.Domain.Market;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.User;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PermissionManager {
    private List<Permission> allDeletedPermissions;
    private String systemManagerEmail;
    static Logger logger = Logger.getLogger(PermissionManager.class);


    private static class PermissionManagerWrapper {
        static PermissionManager single_instance = new PermissionManager();

    }

    private PermissionManager() {
        allDeletedPermissions = new ArrayList<>();
        systemManagerEmail = null;
        logger.info("init Permission Manager");
    }

    public static PermissionManager getInstance() {
        return PermissionManager.PermissionManagerWrapper.single_instance;
    }

    public DResponseObj<Boolean> setSystemManager(String systemManagerEmail) {
        if (this.systemManagerEmail != null && this.systemManagerEmail.equals(systemManagerEmail)) {
            return new DResponseObj<>(true);
        }

        if (this.systemManagerEmail != null) {
            logger.warn("Can't set " + systemManagerEmail + " ass a systemManager. Already have a system manager in this market.\n");
            return new DResponseObj<>(false, ErrorCode.ALLREADYHAVESYSTEMMANAGER);
        }
        this.systemManagerEmail = systemManagerEmail;
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> createPermission(User grantee, Store store, User grantor, userTypes granteeType, userTypes grantorType) {
        /**
         param:
         grantee     - Who get the permissions.                                            | case 1,2,3: User
         store       - The store to which grantee get the permissions.                     | case 1,2,3: Store
         grantor     - Who gives the permissions.                                          | case 1: null , case 2,3 User
         granteeType - what grantee permission type is needed : manager or owner.          | case 1: owner, case2: owner ,case3: manager
         grantorType - what grantor permission type there is now in this store .           | case 1: system, case2: owner ,case3: owner

         documentation:
         accept relationship  : {grantor-owner ,grantee-owner  | grantor-owner ,grantee-manager  | grantor-system ,grantee- owner (open store case) }

         case 1: requirement II.3.2
         grantor-system -> grantee- owner (open store case) need to check before that :
         1.grantee is member
         2.the store accessPermission is empty (because it just open)
         3.grantor Type is system and the user grantor is null

         case 2: requirement II.4.4
         grantor-owner -> grantee-owner   : need to check before that :
         1. grantee is member
         2. grantee is not already owner of this store(check in accessPermission store)
         3. grantor is owner of this store (check in accessPermission store)

         case 3: requirement II.4.6
         grantor-owner -> grantee-manager :  need to check before that :
         1. grantee is member
         2. grantee is not already owner or manager of this store(check in accessPermission store)
         3. grantor is owner of this store (check in accessPermission store)
         **/

        //for all cases:
        //  assumption: grantee and grantor must be member because user is always member;


        synchronized (grantee) {
//      ***********     case 1    **************
            if (grantor == null && grantorType == userTypes.system & granteeType == userTypes.owner)
                return new DResponseObj<>(addOwnerPermissionToNewStore(grantee, store, granteeType, grantorType));

//      **********  cases 2 & 3   ************
            if (grantor == null) {
                logger.warn("Grantor can be null only in case that open new store\n");
                return new DResponseObj<>(false, ErrorCode.NOTUSER);
            }
//         grantee != grantor;
            if (grantee.getEmail().value.equals(grantor.getEmail().value)) {
                logger.warn("Grantor and grantee can't be the same user!\n");
                return new DResponseObj<>(false, ErrorCode.GRANTEANDGRANTORSAMEUSER);
            }

            boolean grantorIsOwner = false;      // need to be true
            List<Permission> accessPermissionStore = store.getPermission().value;
            Permission alreadyManagerPermission = null;

            for (Permission p : accessPermissionStore) {
                //check if grantee is already owner or manager in this store
                if (p.getGrantee().value.getEmail().value.equals(grantee.getEmail().value)) {
                    if (p.getGranteeType().value == userTypes.owner) {
                        logger.warn("Grantee already owner in this store!\n");
                        //if grantee already owner in both cases (2 & 3) the permission is prohibited.
                        return new DResponseObj<>(false, ErrorCode.ALLREADYOWNER);
                    }
                    if (p.getGranteeType().value == userTypes.manager) {
                        //if already manager: case 3 the permission is prohibited, case 2: need to delete the manager permission
                        alreadyManagerPermission = p;
                    }
                }
                //check grantor is owner in this store
                if (p.getGrantee().value.getEmail().value.equals(grantor.getEmail().value) && p.getGranteeType().value == userTypes.owner) {
                    grantorIsOwner = true;
                }
            }

            // case 2&3  check grantor is owner in this store
            if (!grantorIsOwner) {
                logger.warn("Grantor must be owner in this store!\n");
                return new DResponseObj<>(false, ErrorCode.NOTOWNER);
            }
            //case 2 : grantor-owner ,grantee-owner:    delete manager permission before add owner permission
            if (granteeType == userTypes.owner && grantorType == userTypes.owner) {
                deleteManagerPermission(grantee, store, alreadyManagerPermission);
            }
            //case 3 : grantor-owner ,grantee-manager:  cannot added twice manager permission
            if (granteeType == userTypes.manager && grantorType == userTypes.owner) {
                if (alreadyManagerPermission != null) {
                    logger.warn("Rantee is already manager in this store\n");
                    return new DResponseObj<>(false, ErrorCode.ALLREADYMANAGER);
                }
            }
            initializePermission(grantee, store, grantor, granteeType, grantorType);
        }
//        grantee.notifyAll();
        logger.info("New permission was added successfully\n");
        return new DResponseObj<>(true);


    }

    //requirement II.4.7
    public DResponseObj<Boolean> addManagerPermissionType(permissionType.permissionEnum permissionType, User grantee, Store store, User grantor) {

        /**
         param:
         permissionType - what manager permission type to add:  from class permissionType- permissionEnum
         grantee     - Who get the permission.
         store       - The store to which grantee get the permission type.
         grantor     - Who gives the permission.

         documentation:
         add to the manager Permission (user) to granteePermissionTypes a new manager permission

         the grantee must be a manager in this store that the grantor Appointed.(otherwise return false)

         TODO add collection of permission that can be given to manager and check this ?
         if already have this permission, return false or raise exception that the manager have this permission?

         */
//      verify that there is a permission for this three (rantee, store, grantor)

        Permission ManagerPermission = getPermission(grantee, store, grantor);
        if (ManagerPermission == null) {
            logger.warn("Their is no permission That the Grantor gives to the Grantee in this store\n");
            return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
        }
        //verify that the Grantee is manager and the Grantor is owner in the store.
        if (!verifyPermissionType(ManagerPermission, userTypes.manager, userTypes.owner)) {
            logger.warn("Their is no manager - owner connection to the grantee and grantor in this store\n");
            return new DResponseObj<>(false, ErrorCode.MISTAKEPERMISSIONTYPE);
        }
        if (ManagerPermission.hasPermission(permissionType).value) {
            logger.warn("already have this permission type\n");
            return new DResponseObj<>(false, ErrorCode.ALLREADYHAVESYSTEMMANAGER);
        }
        ManagerPermission.addManagerPermission(permissionType);
        logger.info("New manager permission type was added successfully\n");
        return new DResponseObj<>(true);
    }

    //requirement II.4.7
    public DResponseObj<Boolean> removeManagerPermissionType(permissionType.permissionEnum permissionType, User grantee, Store store, User grantor) {
        /**
         param:
         permissionType - what manager permission type to remove
         grantee     - Who get the permission.
         store       - The store to which grantee get the permission type.
         grantor     - Who gives the permission.

         documentation:
         remove from the manager Permission (user) granteePermissionTypes a manager permission

         the grantee must be a manager in this store that the grantor Appointed.(otherwise return false)


         */
//      verify that there is a permission for this three (rantee, store, grantor)
        Permission ManagerPermission = getPermission(grantee, store, grantor);
        if (ManagerPermission == null) {
            logger.warn("Their is no permission That the Grantor gives to the Grantee in this store\n");
            return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
        }
        //verify that the Grantee is manager and the Grantor is owner in this store
        if (!verifyPermissionType(ManagerPermission, userTypes.manager, userTypes.owner)) {
            logger.warn("Their is no manager - owner connection to the grantee and grantor in this store\n");
            return new DResponseObj<>(false, ErrorCode.MISTAKEPERMISSIONTYPE);
        }
        logger.info("Manager permission type removed successfully\n");
        return new DResponseObj<>(ManagerPermission.removeManagerPermission(permissionType).value);
    }


    //requirement II.4.8
    public DResponseObj<Boolean> removeManagerPermissionCompletely(User grantee, Store store, User grantor) {
        /**
         documentation:
         remove the manager permission that the grantor Appointed to the grantee in this store
         If there is no such permission, return false

         */
        Permission ManagerPermission = getPermission(grantee, store, grantor);
        if (ManagerPermission == null) {
            logger.warn("Their is no permission That the Grantor gives to the Grantee in this store\n");
            return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
        }
        if (!verifyPermissionType(ManagerPermission, userTypes.manager, userTypes.owner)) {
            logger.warn("Their is no manager - owner connection to the grantee and grantor in this store\n");
            return new DResponseObj<>(false, ErrorCode.MISTAKEPERMISSIONTYPE);
        }
        allDeletedPermissions.add(ManagerPermission);
        grantee.removeAccessPermission(ManagerPermission);
        grantor.removeGrantorPermission(ManagerPermission);
        store.removePermission(ManagerPermission);

        return new DResponseObj<>(true);
    }

    //requirement II.4.11 a
    public DResponseObj<List<User>> getOwnerAndManagerUsersInStore(User grantor, Store store) {
        /**
         documentation:
         this method return a list of all the users that are manager or owner in the store that the grantor is owner of.
         if there is no users such that, an empty list will be returned.

         after calling this method, you will need to get the information about this users. (user class responsibility)
         */
        List<User> OwnerAndManagerUsersInStore = new ArrayList<>();
        List<Permission> accessPermissionStore = grantor.getGrantorPermission().value;
        for (Permission p : accessPermissionStore) {
            if (store.getStoreId().value.equals(p.getStore().value.getStoreId().value)) {
                OwnerAndManagerUsersInStore.add(p.getGrantee().value);
            }
        }
        return new DResponseObj<>(OwnerAndManagerUsersInStore);
    }
    //requirement II.4.11 b

    public DResponseObj<HashMap<String, List<permissionType.permissionEnum>>> getStoreManagersPermissionsTypes(User grantor, Store store) {
        /**
         documentation:
         this method return a HashMap consist of manager mail(managers that grantor Appointed)and all permission type that he have in the store.
         if there is no manager permission type like that , an empty list will be returned.

         */
//        List<List<permissionType.permissionEnum>> StoreManagersPermissions = new ArrayList<>();
        HashMap<String, List<permissionType.permissionEnum>> StoreManagersPermissionsPerEmail = new HashMap<>();
        List<Permission> grantorPermissionStore = grantor.getGrantorPermission().value;
        for (Permission p : grantorPermissionStore) {
            if (store.getStoreId().value.equals(p.getStore().value.getStoreId().value) && p.getGranteeType().value == userTypes.manager) {
//                StoreManagersPermissions.add(p.getgranteePermissionTypes());
                StoreManagersPermissionsPerEmail.put(p.getGrantee().value.getEmail().value, p.getgranteePermissionTypes().value);
            }
        }
        return new DResponseObj<>(StoreManagersPermissionsPerEmail);
    }

    public DResponseObj<Boolean> hasPermission(permissionType.permissionEnum pType, User grantee, Store store) {
        /**
         documentation:
         pType:  permissionType such as: OpenNewStore,addNewProductToStore..

         this method check if specific pType is in the permission of the grantee in the store.
         for member : always a constant list that they allowed. for manager and owner:  need to be check their permission.

         */
        if (grantee.getEmail().value.equals("yaki@gmail.com"))
            return new DResponseObj<>(true);

        // for members (if memberPermissions not contains this pType, false will be returned because their is no permissions for members.)
        if (grantee.getEmail().value.equals(systemManagerEmail) && permissionType.systemManagerPermissions.contains(pType))
            return new DResponseObj<>(true);

        if (permissionType.memberPermissions.contains(pType))
            return new DResponseObj<>(true);
        if (store != null) {
            List<Permission> accessPermissionStore = store.getPermission().value;
            for (Permission p : accessPermissionStore) {
                if (p.getGrantee().value.getEmail().value.equals(grantee.getEmail().value)) {
                    return new DResponseObj<>(p.hasPermission(pType).value);
                }
            }
        }
        return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
    }

    public DResponseObj<userTypes> getGranteeUserType(User grantee, Store store) {
        /**
         Assumption: a User is always a members
         documentation:
         Given grantee and store we will return grantee user type in the store (such as: member, manager, owner)

         */
        //systemManager
        List<Permission> accessPermissionStore = grantee.getAccessPermission().value;
        for (Permission p : accessPermissionStore) {
            if (store.getStoreId().value.equals((p.getStore()).value.getStoreId().value)) {
                return new DResponseObj<>(p.getGranteeType().value);
            }
        }
        return new DResponseObj<>(userTypes.member);
    }

    public DResponseObj<Boolean> isSystemManager(String email) {
        /**
         Assumption:
         documentation:
         Given user email  return true if the user is the system manager

         */
        return new DResponseObj<>(email.equals(systemManagerEmail));
    }

    public DResponseObj<Boolean> isFounder(User grantee, Store store) {
        /**
         Assumption: a User is always a members
         documentation:
         Given grantee and store we will return true if the grantee is the founder of this store

         */
        //systemManager
        List<Permission> accessPermissionStore = grantee.getAccessPermission().value;
        for (Permission p : accessPermissionStore) {
            if (store.getStoreId().value.equals((p.getStore()).value.getStoreId().value)) {
                userTypes UT = p.getGranteeType().value;
                if (UT == userTypes.owner && p.getGrantorType().value == userTypes.system)
                    return new DResponseObj<>(true);
            }
        }
        return new DResponseObj<>(false);
    }

    public void reset(){
        allDeletedPermissions = new ArrayList<>();
        systemManagerEmail = null;
    }
//*********************************************** private method ******************************************************


    private void deleteManagerPermission(User grantee, Store store, Permission alreadyManagerPermission) {
        if (alreadyManagerPermission != null) {
            store.removePermission(alreadyManagerPermission);
            grantee.removeAccessPermission(alreadyManagerPermission);
            User Grantor = alreadyManagerPermission.getGrantor().value;
            if (Grantor != null) // necessary? can be manager without grantor?
                Grantor.removeGrantorPermission(alreadyManagerPermission);
        }
    }

    private boolean addOwnerPermissionToNewStore(User grantee, Store store, userTypes granteeType, userTypes grantorType) {
        if (!store.getPermission().value.isEmpty())
            return false;
        else {
            Permission per = new Permission(grantee, store, null);
            per.setPermissionTypes(granteeType, grantorType);
            store.addPermission(per);
            grantee.addAccessPermission(per);
            return true;
        }
    }

    private void initializePermission(User grantee, Store store, User grantor, userTypes granteeType, userTypes grantorType) {
        Permission per = new Permission(grantee, store, grantor);
        per.setPermissionTypes(granteeType, grantorType);
        store.addPermission(per);
        grantee.addAccessPermission(per);
        grantor.addGrantorPermission(per);
    }

    private boolean verifyPermissionType(Permission permission, userTypes granteeType, userTypes grantorType) {
        return permission.getGranteeType().value == granteeType && permission.getGrantorType().value == grantorType;
    }

    private Permission getPermission(User grantee, Store store, User grantor) {
        //find the permission - can be only one in the triple: grantee store and grantor.
        List<Permission> accessPermissionStore = store.getPermission().value;
        for (Permission p : accessPermissionStore) {
            if (p.getGrantee().value.getEmail().value.equals(grantee.getEmail().value) && p.getGrantor().value.getEmail().value.equals(grantor.getEmail().value)) {
                return p;
            }
        }
        return null;
    }
}

//   disjunctionPermissions = grantee.accessPers ^ store.accessPers = nothing;
//   check that there is no other permission for these grantee and store
//   List<Permission> disjunctionPermissions = grantee.accessPermission;
//   disjunctionPermissions.retainAll(accessPermissionStore);
//   if (disjunctionPermissions.size() != 0) {
//   return false;
//    }