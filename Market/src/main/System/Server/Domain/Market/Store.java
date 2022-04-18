package main.System.Server.Domain.Market;

import main.System.Server.Domain.UserComponent.Response.StoreResponse;

import java.util.List;

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
}
