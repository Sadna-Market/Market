package main.System.Server.Domain.UserComponent;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ShoppingCart;
import main.System.Server.Domain.Market.Store;

import java.util.List;

public class User {
    ShoppingCart shoppingCart;
    List<Permission> ManagerPermission;
    List<Permission> grantorPermission;


    public ShoppingCart getShoppingCart()
    {
        return shoppingCart;
    }

    public boolean addFounder(Store store) {
        return false;
    }

    public boolean addNewStoreOwner(User user, Store store) {
        return false;
    }

    public boolean addNewStoreManager(User user, Store store) {
        return false;
    }

    public boolean setManagerPermissions(User user, Store store) {
        return false;
    }

    public boolean getRolesInStore(Store store) {
        return false;
    }
}
