package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Store {
    Inventory inventory ;
    int StoreId;
    List<Permission> ManagerPermission;

    public Store(DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
    }

    public Store(String name, String founder, DiscountPolicy discountPolicy, BuyPolicy buyPolicy, BuyStrategy buyStrategy) {
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

    public boolean addNewProduct(ProductType productId, double price, int quantity) {
        return true;
    }

    public boolean removeProduct(ProductType productId) {
       // return inventory.removeProduct(productId);
        return true;
    }

    public boolean setProduct(ProductType productId, String productName, String category, int price, int quantity, String description) {
        return true;
    }

    public List<History> getStoreOrderHistory() {
        return new ArrayList<>();
    }

    public int getRate() {
        return rate;
    }

    public Double getProductPrice(int productID) {
        return 3.0;
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

    public boolean setProductPrice(int productId, double price) {
        return true;
    }

    public boolean setProductQuantity(int productId, int quantity) {
        return true;
    }

    public void getUserHistory(String s) {
    }

    public static class BuyPolicy {
    }

}
