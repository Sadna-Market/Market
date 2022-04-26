package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.User;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    private User grantee;// the permission is for him
    private Store store;
    private User grantor;//who give the permission
    private List<permissionType.permissionEnum> granteePermissionTypes;

    private userTypes granteeType;
    private userTypes grantorType;

    Permission(User grantee, Store store, User grantor) {
        this.grantee = grantee;
        this.store = store;
        this.grantor = grantor;
        granteePermissionTypes = new ArrayList<>();
        granteeType = null;
        grantorType = null;
    }


    public void setPermissionTypes(userTypes granteeType, userTypes grantorType) {
        //case 1: grantor-system ,grantee- owner
        if ((granteeType == userTypes.owner) && (grantorType == userTypes.system)) {
            this.granteePermissionTypes = permissionType.ownerPermissions;
            this.granteeType = userTypes.owner;
            this.grantorType = userTypes.system;
        }


        //case 2: grantor-owner ,grantee-owner
        else if ((granteeType == userTypes.owner) && (grantorType == userTypes.owner)) {
            this.granteePermissionTypes = permissionType.ownerPermissions;
            this.granteeType = userTypes.owner;
            this.grantorType = userTypes.owner;
        }

        //case 3: grantor-owner ,grantee-manager
        else if ((granteeType == userTypes.manager) && (grantorType == userTypes.owner)) {
            this.granteePermissionTypes = permissionType.managerPermissions;
            this.granteeType = userTypes.manager;
            this.grantorType = userTypes.owner;
        }
    }

    boolean hasPermission(permissionType.permissionEnum permissionType) {
        return granteePermissionTypes.contains(permissionType);
    }

    public void addManagerPermission(permissionType.permissionEnum permissionType) {
        granteePermissionTypes.add(permissionType);
    }

    public boolean removeManagerPermission(permissionType.permissionEnum permissionType) {
        return granteePermissionTypes.remove(permissionType);
    }

    public Store getStore() {
        return store;
    }

    public User getGrantee() {
        return grantee;
    }

    public User getGrantor() {
        return grantor;
    }

    public userTypes getGranteeType() {
        return granteeType;
    }

    public userTypes getGrantorType() {
        return grantorType;
    }
    public List<permissionType.permissionEnum> getgranteePermissionTypes() {
        return granteePermissionTypes;
    }
}
