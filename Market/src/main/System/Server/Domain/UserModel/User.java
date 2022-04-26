package main.System.Server.Domain.UserModel;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.PermissionManager;
import main.System.Server.Domain.Market.permissionType;
import main.System.Server.Domain.Market.userTypes;
import main.System.Server.Domain.StoreModel.Store;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class User extends Guest{
    ShoppingCart shoppingCart;
    String Password;
    String email;
    String phoneNumber;
    String CreditCard;
    String CreditDate;

    private List<Permission> accessPermission= new LinkedList<>();; // all the permission is my
    private List<Permission> grantorPermission= new LinkedList<>();;//all the permission that this user gave
    private List<Permission> safeAccessPermission = Collections.synchronizedList(accessPermission);
    private List<Permission> safeGrantorPermission = Collections.synchronizedList(grantorPermission);

    public User(String email,String password ,String phoneNumber,String CreditCared,String CreditDate) {
        shoppingCart=new ShoppingCart();
        this.email=email;
        this.Password=password;
        this.phoneNumber = phoneNumber;
        this.CreditCard = CreditCared;
        this.CreditDate= CreditDate;

    }

    public void addAccessPermission(Permission p){
        accessPermission.add(p);
    }
    public void addGrantorPermission(Permission p){
        grantorPermission.add(p);
    }
    public void removeAccessPermission(Permission p){
        accessPermission.remove(p);
    }
    public void removeGrantorPermission(Permission p){
        grantorPermission.remove(p);
    }
    public String getEmail(){
        return email;
    }

    public List<Permission> getAccessPermission(){
        return accessPermission;
    }
    public List<Permission> getGrantorPermission(){
        return grantorPermission;
    }


    //all the store that i have a permission
    List<Store> granteeStores() {
        List<Store> granteeStores=new LinkedList<>();
        for (Permission permission  : accessPermission)
        {
            granteeStores.add(permission.getStore());
        }
        return granteeStores;
    }

    public boolean addFounder(Store store) {
        return PermissionManager.getInstance().createPermission(this,store,null,userTypes.owner,userTypes.system);
    }

    public boolean addNewStoreOwner(User user, Store store) {
        PermissionManager permissionManager =PermissionManager.getInstance();
        return (permissionManager.createPermission(user,store,this, userTypes.owner,userTypes.owner));
    }

    public boolean addNewStoreManager(User user, Store store) {
        PermissionManager permissionManager =PermissionManager.getInstance();
        return (permissionManager.createPermission(user,store,this, userTypes.manager,userTypes.owner));    }

    public boolean setManagerPermissions(User user, Store store, permissionType.permissionEnum perm) {
        PermissionManager permissionManager =PermissionManager.getInstance();
        return (permissionManager.addManagerPermissionType(perm,user,store,this));
    }

    public boolean getRolesInStore(Store store){
        return false;
    }

    public  boolean isPasswordEquals(String password){
        return this.Password.equals( password);
    }

    public ShoppingCart getShoppingCart(){
        return this.shoppingCart;
    }
}
