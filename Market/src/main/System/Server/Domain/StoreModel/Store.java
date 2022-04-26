package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.UserModel.User;

import java.util.LinkedList;
import java.util.List;

public class Store {
    Inventory inventory ;
    int StoreId;

    private List<Permission> accessPermission; // all the permission that have in this store


    public void addAccessPermission(Permission p){
        accessPermission.add(p);
    }
    public void removeAccessPermission(Permission p){
        accessPermission.remove(p);
    }
    public List<Permission> getAccessPermission(){
        return accessPermission;
    }

    //all the user that have a permission in this store
    public  List<User> getGranteeUsers() {
        List<User> granteeUser=new LinkedList<>();
        for (Permission permission  : accessPermission)
        {
            granteeUser.add(permission.getGrantee());
        }

        return granteeUser;
    }
    public int getStoreId(){
        return StoreId;
    }

    public Store GetStoreInfo() {
        return null;
    }


    public boolean isProductExistInStock(int productId, int quantity){
        return inventory.isProductExistInStock(productId,quantity);
    }

    public boolean addNewProduct(int productId, String productName, String categori, double price, int quantity, String description) {
        return inventory.addNewProduct(productId, productName, categori, price, quantity, description);
    }

    public boolean removeProduct(int productId) {
        return inventory.removeProduct(productId);
    }

    public boolean setProduct(int productId, String productName, String category, int price, int quantity, String description) {
        return inventory.setProduct(productId, productName, category, price, quantity, description);
    }

    public boolean getStoreOrderHistory() {
        return false;
    }

    public static class BuyPolicy {
    }
}
