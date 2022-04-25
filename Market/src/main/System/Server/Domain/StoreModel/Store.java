package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;

import java.util.List;
import java.util.Random;

public class Store {
    Inventory inventory ;
    int StoreId;
    List<Permission> ManagerPermission;

    public Store(DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
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

    public boolean addNewProduct(ProductType productId, String productName, String categori, double price, int quantity, String description) {
        return true;
    }

    public boolean removeProduct(ProductType productId) {
       // return inventory.removeProduct(productId);
        return true;
    }

    public boolean setProduct(ProductType productId, String productName, String category, int price, int quantity, String description) {
        return true;
    }

    public boolean getStoreOrderHistory() {
        return true;
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

    public String getProductInStoreInfo(int productID) {
        return "";
    }

    public boolean closeStore() {
        return true;
    }

    public static class BuyPolicy {
    }

}
