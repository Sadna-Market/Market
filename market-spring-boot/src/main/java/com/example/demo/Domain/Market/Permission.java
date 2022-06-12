package com.example.demo.Domain.Market;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.User;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    private User grantee;// the permission is for him
    private Store store;

    public void setGrantor(User grantor) {
        this.grantor = grantor;
    }

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
    //createPermission;

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

    DResponseObj<Boolean> hasPermission(permissionType.permissionEnum permissionType) {
        return new DResponseObj<>(granteePermissionTypes.contains(permissionType));
    }

    public void addManagerPermission(permissionType.permissionEnum permissionType) {
        granteePermissionTypes.add(permissionType);
    }

    public DResponseObj<Boolean> removeManagerPermission(permissionType.permissionEnum permissionType) {
        return new DResponseObj<>(granteePermissionTypes.remove(permissionType));
    }

    public DResponseObj<Store> getStore() {
        return new DResponseObj<>(store);
    }

    public DResponseObj<User> getGrantee() {
        return new DResponseObj<>(grantee);
    }

    public DResponseObj<User> getGrantor() {
        return new DResponseObj<>(grantor);
    }

    public DResponseObj<userTypes> getGranteeType() {
        return new DResponseObj<>(granteeType);
    }

    public DResponseObj<userTypes> getGrantorType() {
        return new DResponseObj<>(grantorType);
    }

    public DResponseObj<List<permissionType.permissionEnum>> getgranteePermissionTypes() {
        return new DResponseObj<>(granteePermissionTypes);
    }
}
