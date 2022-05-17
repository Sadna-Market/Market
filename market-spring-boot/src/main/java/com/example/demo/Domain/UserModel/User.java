package com.example.demo.Domain.UserModel;


import com.example.demo.Domain.Market.Permission;
import com.example.demo.Domain.Market.PermissionManager;
import com.example.demo.Domain.Market.permissionType;
import com.example.demo.Domain.Market.userTypes;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.History;
import com.example.demo.Domain.StoreModel.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User  {
    ShoppingCart shoppingCart;
    String Password;
    String email;
    String phoneNumber;
    private List<History> histories = Collections.synchronizedList(new LinkedList<>());



    private List<Permission> accessPermission = new LinkedList<>();
    ; // all the permission is my
    private List<Permission> grantorPermission = new LinkedList<>();
    ;//all the permission that this user gave
    private List<Permission> safeAccessPermission = Collections.synchronizedList(accessPermission);
    private List<Permission> safeGrantorPermission = Collections.synchronizedList(grantorPermission);

    public User(String email, String password, String phoneNumber) {
        shoppingCart = new ShoppingCart();
        this.email = email;
        this.Password = password;
        this.phoneNumber = phoneNumber;
        shoppingCart=new ShoppingCart();


    }
    public User(){
        shoppingCart=new ShoppingCart();
        email = "guest";
        this.Password="guest";
        this.phoneNumber="0000000000";

    }
    public DResponseObj<Boolean> addAccessPermission(Permission p) {
        accessPermission.add(p);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> addGrantorPermission(Permission p) {
        grantorPermission.add(p);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> addHistoriy(History history){
        histories.add(history);
        return new DResponseObj<>(true,-1);
    }


    public DResponseObj<List<History>> getAllHistories(){
        return new DResponseObj<List<History>>(histories,-1);
    }


    public DResponseObj<Boolean> removeAccessPermission(Permission p) {
        accessPermission.remove(p);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> removeGrantorPermission(Permission p) {
        grantorPermission.remove(p);
        return new DResponseObj<>(true);
    }

    public DResponseObj<String> getEmail() {
        return new DResponseObj<>( email);
    }

    public DResponseObj<List<Permission>> getAccessPermission() {
        return new DResponseObj<>( accessPermission);
    }

    public DResponseObj<List<Permission>> getGrantorPermission() {
        return new DResponseObj<>( grantorPermission);
    }


    //all the store that i have a permission
    public DResponseObj< List<Store>> granteeStores() {
        List<Store> granteeStores = new LinkedList<>();
        for (Permission permission : accessPermission) {
            granteeStores.add(permission.getStore().value);
        }
        return new DResponseObj<>( granteeStores);
    }

    public DResponseObj<Boolean> addFounder(Store store) {
        return PermissionManager.getInstance().createPermission(this, store, null, userTypes.owner, userTypes.system);
    }

    public DResponseObj<Boolean> addNewStoreOwner(User user, Store store) {
        PermissionManager permissionManager = PermissionManager.getInstance();
        return (permissionManager.createPermission(user, store, this, userTypes.owner, userTypes.owner));
    }

    public DResponseObj<Boolean> addNewStoreManager(User user, Store store) {
        PermissionManager permissionManager = PermissionManager.getInstance();
        return (permissionManager.createPermission(user, store, this, userTypes.manager, userTypes.owner));
    }

    public DResponseObj<Boolean> setManagerPermissions(User user, Store store, permissionType.permissionEnum perm, boolean onOf) {
        if(onOf) {
            PermissionManager permissionManager = PermissionManager.getInstance();
            DResponseObj<Boolean> b = permissionManager.addManagerPermissionType(perm, user, store, this);
            return b;
        }else {
            PermissionManager permissionManager = PermissionManager.getInstance();
            DResponseObj<Boolean> b = permissionManager.removeManagerPermissionType(perm, user, store, this);
            return b;
        }
    }

    public DResponseObj<Boolean> changePassword(String password){
        this.Password=password;
        return new DResponseObj<>(true);
    }

    public DResponseObj< Boolean > isPasswordEquals(String password) {
        return new DResponseObj<>( this.Password.equals(password));
    }

    public DResponseObj<ShoppingCart> GetSShoppingCart() {
        return new DResponseObj<>( this.shoppingCart);
    }

    public DResponseObj<Boolean> addHistoies(List<History> histories) {
        for(History h :histories){
            this.histories.add(h);
        }
        return new DResponseObj<>(true,-1);
    }
}