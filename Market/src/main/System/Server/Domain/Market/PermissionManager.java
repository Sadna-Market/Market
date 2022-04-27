package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.Response.ATResponseObj;
import main.System.Server.Domain.UserModel.User;
import main.System.Server.Domain.UserModel.userTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// לבדוק נלים בכל פונקציה?
// מה קורה עם אקספשנים ו רסיפונסים וכ
// TODO טסטים
// נסגרת חנות : מה קורה עם ההרשאה ? כי ליוזר יש רשימה של הרשאות שיש לו ובה את ההרשאה בחנות שנסגרה, צריך למחוק אותה?מה קורה אן ירצו לשחזר-יעני פתיחת חנות מחדש כמו בדרישה?
//תשובה - תבדקו לפני שהחנות פתוחה לפני שאתם בודקים הרשאות ושיט בכלל - ההרשאות לא ימחקו

// מנהל מערכת ? מה איתו במחינת הרשאות ? יש לו הכל ? מי ממנה אותו ? רלוונטי בכלל להרשאות?


//ביטול מנוי של מייסד חנות -> מה קורה עם החנות ? מי בעלים ? מי יוכל למחוק אותה אם ירצה? או שהחנות נסגרת ?
// תשובה - מבטל את כל ההרשאות של כל מי שבחנו
public class PermissionManager {
    private List<Permission> allDeletedPermissions;

    private static class PermissionManagerWrapper {
        static PermissionManager single_instance = new PermissionManager();

    }

    private PermissionManager() {
        allDeletedPermissions = new ArrayList<>();
    }

    public static PermissionManager getInstance() {
        return PermissionManager.PermissionManagerWrapper.single_instance;
    }


    public ATResponseObj<Boolean> createPermission(User grantee, Store store, User grantor, userTypes granteeType, userTypes grantorType) {
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
                return new ATResponseObj<>(addOwnerPermissionToNewStore(grantee, store, granteeType, grantorType));

//      **********  cases 2 & 3   ************
            if (grantor == null)
                return new ATResponseObj<>(false, "grantor can be null only in case that open new store");

//         grantee != grantor;
            if (grantee.getEmail().equals(grantor.getEmail()))
                return new ATResponseObj<>(false, "grantor and grantee can't be the same user!");

            boolean grantorIsOwner = false;      // need to be true
            List<Permission> accessPermissionStore = store.getPermission();
            Permission alreadyManagerPermission = null;

            for (Permission p : accessPermissionStore) {
                //check if grantee is already owner or manager in this store
                if (p.getGrantee().getEmail().equals(grantee.getEmail())) {
                    if (p.getGranteeType() == userTypes.owner)
                        //if grantee already owner in both cases (2 & 3) the permission is prohibited.
                        return new ATResponseObj<>(false, "grantee already owner in this store!");
                    ;
                    if (p.getGranteeType() == userTypes.manager) {
                        //if already manager: case 3 the permission is prohibited, case 2: need to delete the manager permission
                        alreadyManagerPermission = p;
                    }
                }
                //check grantor is owner in this store
                if (p.getGrantee().getEmail().equals(grantor.getEmail()) && p.getGranteeType() == userTypes.owner)
                    grantorIsOwner = true;
            }

            // case 2&3  check grantor is owner in this store
            if (!grantorIsOwner) return new ATResponseObj<>(false, "grantor must be owner in this store!");

            //case 2 : grantor-owner ,grantee-owner:    delete manager permission before add owner permission
            if (granteeType == userTypes.owner && grantorType == userTypes.owner) {
                deleteManagerPermission(grantee, store, alreadyManagerPermission);
            }
            //case 3 : grantor-owner ,grantee-manager:  cannot added twice manager permission
            if (granteeType == userTypes.manager && grantorType == userTypes.owner) {
                if (alreadyManagerPermission != null) {
                    return new ATResponseObj<>(false, "rantee is already manager in this store");
                }
            }
            initializePermission(grantee, store, grantor, granteeType, grantorType);
        }
//        grantee.notifyAll();
        return new ATResponseObj<>(true);


    }

    //requirement II.4.7
    public ATResponseObj<Boolean> addManagerPermissionType(permissionType.permissionEnum permissionType, User grantee, Store store, User grantor) {

/*
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
        if (ManagerPermission == null) return new ATResponseObj<>(false, "their is no permission That the Grantor gives to the Grantee in this store");
        //verify that the Grantee is manager and the Grantor is owner in the store.
        if (!verifyPermissionType(ManagerPermission, userTypes.manager, userTypes.owner))
            return new ATResponseObj<>(false, "their is no manager - owner connection to the grantee and grantor in this store");

        ManagerPermission.addManagerPermission(permissionType);
        return new ATResponseObj<>(true);
    }

    //requirement II.4.7
    public ATResponseObj<Boolean> removeManagerPermissionType(permissionType.permissionEnum permissionType, User grantee, Store store, User grantor) {
/*
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
        if (ManagerPermission == null)
        return new ATResponseObj<>(false, "their is no permission That the Grantor gives to the Grantee in this store");
        //verify that the Grantee is manager and the Grantor is owner in this store
        if (!verifyPermissionType(ManagerPermission, userTypes.manager, userTypes.owner))
            return new ATResponseObj<>(false, "their is no manager - owner connection to the grantee and grantor in this store");

        return new ATResponseObj<>(ManagerPermission.removeManagerPermission(permissionType));
    }


    //requirement II.4.8
    public ATResponseObj<Boolean> removeManagerPermissionCompletely(User grantee, Store store, User grantor) {
/*
     documentation:
     remove the manager permission that the grantor Appointed to the grantee in this store
     If there is no such permission, return false

 */
        Permission ManagerPermission = getPermission(grantee, store, grantor);
        if (ManagerPermission == null)  return new ATResponseObj<>(false, "their is no permission That the Grantor gives to the Grantee in this store");

        if (!verifyPermissionType(ManagerPermission, userTypes.manager, userTypes.owner))
            return new ATResponseObj<>(false, "their is no manager - owner connection to the grantee and grantor in this store");

        allDeletedPermissions.add(ManagerPermission);
        grantee.removeAccessPermission(ManagerPermission);
        grantor.removeGrantorPermission(ManagerPermission);
        store.removePermission(ManagerPermission);

        return new ATResponseObj<>(true);
    }

    //requirement II.4.11 a
    public List<User> getOwnerAndManagerUsersInStore(User grantor, Store store) {
/*
     documentation:
     this method return a list of all the users that are manager or owner in the store that the grantor is owner of.
     if there is no users such that, an empty list will be returned.

     after calling this method, you will need to get the information about this users. (user class responsibility)
 */
        List<User> OwnerAndManagerUsersInStore = new ArrayList<>();
        List<Permission> accessPermissionStore = grantor.getGrantorPermission();
        for (Permission p : accessPermissionStore) {
            if (store.getStoreId() == p.getStore().getStoreId()) {
                OwnerAndManagerUsersInStore.add(p.getGrantee());
            }
        }
        return OwnerAndManagerUsersInStore;
    }
    //requirement II.4.11 b

    public HashMap<String, List<permissionType.permissionEnum>> getStoreManagersPermissionsTypes(User grantor, Store store) {
/*
     documentation:
     this method return a HashMap consist of manager mail(managers that grantor Appointed)and all permission type that he have in the store.
     if there is no manager permission type like that , an empty list will be returned.

 */
//        List<List<permissionType.permissionEnum>> StoreManagersPermissions = new ArrayList<>();
        HashMap<String, List<permissionType.permissionEnum>> StoreManagersPermissionsPerEmail = new HashMap<>();
        List<Permission> grantorPermissionStore = grantor.getGrantorPermission();
        for (Permission p : grantorPermissionStore) {
            if (store.getStoreId() == p.getStore().getStoreId() && p.getGranteeType() == userTypes.manager) {
//                StoreManagersPermissions.add(p.getgranteePermissionTypes());
                StoreManagersPermissionsPerEmail.put(p.getGrantee().getEmail(), p.getgranteePermissionTypes());
            }
        }
        return StoreManagersPermissionsPerEmail;
    }

    public boolean hasPermission(permissionType.permissionEnum pType, User grantee, Store store) {
/*
     documentation:
     pType:  permissionType such as: OpenNewStore,addNewProductToStore..

     this method check if specific pType is in the permission of the grantee in the store.
     for member : always a constant list that they allowed. for manager and owner:  need to be check their permission.

 */
        // for members (if memberPermissions not contains this pType, false will be returned because their is no permissions for members.)
        if (permissionType.memberPermissions.contains(pType)) return true;

        List<Permission> accessPermissionStore = store.getPermission();
        for (Permission p : accessPermissionStore) {
            if (p.getGrantee().getEmail().equals(grantee.getEmail())) {
                return p.hasPermission(pType);
            }
        }
        return false;

    }

    public userTypes getGranteeUserType(User grantee, Store store) {
/*
     Assumption: a User is always a members
     documentation:
     Given grantee and store we will return grantee user type in the store (such as: member, manager, owner)

 */
        List<Permission> accessPermissionStore = grantee.getAccessPermission();
        for (Permission p : accessPermissionStore) {
            if (store.getStoreId() == p.getStore().getStoreId()) {
                return p.getGranteeType();
            }
        }
        return userTypes.member;
    }


//*********************************************** private method ******************************************************


    private void deleteManagerPermission(User grantee, Store store, Permission alreadyManagerPermission) {
        if (alreadyManagerPermission != null) {
            store.removePermission(alreadyManagerPermission);
            grantee.removeAccessPermission(alreadyManagerPermission);
            User Grantor = alreadyManagerPermission.getGrantor();
            if (Grantor != null) // necessary? can be manager without grantor?
                Grantor.removeGrantorPermission(alreadyManagerPermission);
        }
    }

    private boolean addOwnerPermissionToNewStore(User grantee, Store store, userTypes granteeType, userTypes grantorType) {
        if (!store.getPermission().isEmpty())
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
        return permission.getGranteeType() == granteeType && permission.getGrantorType() == grantorType;
    }

    private Permission getPermission(User grantee, Store store, User grantor) {
        //find the permission - can be only one in the triple: grantee store and grantor.
        List<Permission> accessPermissionStore = store.getPermission();
        for (Permission p : accessPermissionStore) {
            if (p.getGrantee().getEmail().equals(grantee.getEmail()) && p.getGrantor().getEmail().equals(grantor.getEmail())) {
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
