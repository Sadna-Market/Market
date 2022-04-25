package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.Permission;

import javax.print.ServiceUI;
import java.util.List;
import java.util.Random;

public class Store {
    Inventory inventory ;
    int StoreId;
    List<Permission> ManagerPermission;

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

    public int getRate() {
        return rate;
    }

    public int getProductPrice(int productID) {
        return new Random().nextInt(100);
    }
    int rate=0;

    public void setRate(int r){
        rate=r;
    }

    public static class BuyPolicy {
    }

}
