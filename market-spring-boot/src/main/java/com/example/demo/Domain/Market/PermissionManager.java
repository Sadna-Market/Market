package com.example.demo.Domain.Market;

import com.example.demo.DataAccess.Services.DataServices;
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
    private static DataServices dataServices;


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

    //requirement II.4.5
    public DResponseObj<Boolean> removeOwnerPermissionCompletely(User grantee, Store store, User grantor) {
        /**
         documentation:
         remove the owner permission that the grantor Appointed to the grantee in this store
         If there is no such permission, return false
         */
        Permission ownerPermission = getPermission(grantee, store, grantor);
        if (ownerPermission == null) {
            logger.warn("Their is no permission That the Grantor gives to the Grantee in this store\n");
            return new DResponseObj<>(false, ErrorCode.NOPERMISSION);
        }
        if (!verifyPermissionType(ownerPermission, userTypes.owner, userTypes.owner)) {
            logger.warn("Their is no owner - owner connection to the grantee and grantor in this store\n");
            return new DResponseObj<>(false, ErrorCode.MISTAKEPERMISSIONTYPE);
        }
        if (isFounder(grantee, store).value) {
            logger.warn("can't remove permission founder\n");
            return new DResponseObj<>(false, ErrorCode.CAN_NOT_REMOVE_FOUNDER_PERMISSION);
        }
        allDeletedPermissions.add(ownerPermission);
        grantee.removeAccessPermission(ownerPermission);
        grantor.removeGrantorPermission(ownerPermission);
        store.removePermission(ownerPermission);
        //db
        removePermissionFromDB(store, ownerPermission);
        return new DResponseObj<>(true);
    }

    private void removePermissionFromDB(Store store, Permission ownerPermission) {
        if (dataServices != null && dataServices.getPermissionService() != null) {
            var pid = ownerPermission.getPermissionId();
            if (!dataServices.getPermissionService().deletePermission(pid)) {
                logger.error(String.format("failed to remove permission of (grantee,grantor,store)" +
                                "~(%s,%s,%s) -> granteeType %s and grantorType: %s",
                        ownerPermission.getGrantee().value.getEmail().value,
                        ownerPermission.getGrantor().value.getEmail().value,
                        store.getStoreId().value,
                        ownerPermission.getGranteeType().value.name(),
                        ownerPermission.getGrantorType().value.name()
                ));
            }
        }
    }

/*
    private DResponseObj<Boolean> removeOwnerRecursive(User ownerThatRemoved,Store store){
        List<Permission> needToDelete = getPermission(ownerThatRemoved,store);
        DResponseObj<Boolean> res = null;
        for(Permission p :needToDelete){
            res = removeOwnerPermissionCompletely(p.getGrantee().value,p.getStore().value,p.getGrantor().value);
            if(res.errorOccurred()) return res;
        }
        return res == null ? new DResponseObj<>(true) : res;
    }
*/


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
        //db
        updatedPermissionInDB(permissionType,ManagerPermission);
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
        var res = ManagerPermission.removeManagerPermission(permissionType);
        if (!res.value) return res;
        updatedPermissionInDB(permissionType, ManagerPermission);
        return res;
    }

    private void updatedPermissionInDB(permissionType.permissionEnum permissionType, Permission ManagerPermission) {
        if (dataServices != null && dataServices.getPermissionService() != null) {
            var data = ManagerPermission.getDataObject();
            if (!dataServices.getPermissionService().updatePermissionType(data)) {
                logger.error(String.format("failed to update manager permission of user %s the permission type %s",
                        ManagerPermission.getGrantee().value.getEmail().value,
                        permissionType.name()
                ));
            }
        }
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
        //db
        removePermissionFromDB(store, ManagerPermission);
        return new DResponseObj<>(true);
    }

    public DResponseObj<List<Store>> removeAllPermissions(User toCancelUser) {
        List<Store> deleteStore = new ArrayList<>();
        List<Permission> allPermissions = toCancelUser.getAccessPermission().value;
        allPermissions.forEach(permission -> {
            if (permission.getGrantor().value == null) {  //means that the user is a founder of the store
                //add to close the store
                deleteStore.add(permission.getStore().value);
            } else { //if the toCancelUser is not a founder only update the store and the grantor permissions
                permission.getStore().value.removePermission(permission);
                permission.getGrantor().value.removeGrantorPermission(permission);
            }
        });
        return new DResponseObj<>(deleteStore, -1);
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


    /**
     * gets all owners of this store including the contributor of the store.
     *
     * @param store
     * @param type
     * @return list of user of type in store
     */
    public DResponseObj<List<User>> getAllUserByTypeInStore(Store store, userTypes type) {
        List<User> users = new ArrayList<>();
        List<Permission> accessPermissionStore = store.getSafePermission().value;
        for (Permission p : accessPermissionStore) {
            if (p.getGranteeType().value.equals(type))
                users.add(p.getGrantee().value);
        }
        return new DResponseObj<>(users);
    }


    public void reset() {
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
            //db
            removePermissionFromDB(store, alreadyManagerPermission);
        }
    }

    private boolean addOwnerPermissionToNewStore(User grantee, Store store, userTypes granteeType, userTypes grantorType) {
        if (!store.getPermission().value.isEmpty())
            return false;
        else {
            Permission per = new Permission(grantee, store, null);
            per.setPermissionTypes(granteeType, grantorType);
            //db
            if (dataServices != null && dataServices.getPermissionService() != null) {
                var dataPermission = per.getDataObject();
                if (!dataServices.getPermissionService().insertPermission(dataPermission)) {
                    logger.error(String.format("failed to add permission of owner %s to new store %d",
                            grantee.getEmail().value,
                            store.getStoreId().value));
                }
            }
            store.addPermission(per); //on load this per will be added to store
            grantee.addAccessPermission(per); //on load this per will be added to store
            return true;
        }
    }

    private void initializePermission(User grantee, Store store, User grantor, userTypes granteeType, userTypes grantorType) {
        Permission per = new Permission(grantee, store, grantor);
        per.setPermissionTypes(granteeType, grantorType);
        //db
        if (dataServices != null && dataServices.getPermissionService() != null) {
            var dataPermission = per.getDataObject();
            if (!dataServices.getPermissionService().insertPermission(dataPermission)) {
                logger.error(String.format("failed to add permission of granteeType: %s and grantorType: %s in store %d",
                        granteeType.name(),
                        grantorType.name(),
                        store.getStoreId().value));
            }
        }
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

    public static void setDataServices(DataServices dataServices) {
        PermissionManager.dataServices = dataServices;
    }

/*    private List<Permission> getPermission(User grantee, Store store) {
        List<Permission> permissionsOfGrantee = new ArrayList<>();
        List<Permission> accessPermissionStore = store.getPermission().value;
        for (Permission p : accessPermissionStore) {
            if (p.getGrantor().value != null && p.getGrantor().value.getEmail().value.equals(grantee.getEmail().value)) {
                permissionsOfGrantee.add(p);
            }
        }
        return permissionsOfGrantee;
    }*/
}

//   disjunctionPermissions = grantee.accessPers ^ store.accessPers = nothing;
//   check that there is no other permission for these grantee and store
//   List<Permission> disjunctionPermissions = grantee.accessPermission;
//   disjunctionPermissions.retainAll(accessPermissionStore);
//   if (disjunctionPermissions.size() != 0) {
//   return false;
//    }
