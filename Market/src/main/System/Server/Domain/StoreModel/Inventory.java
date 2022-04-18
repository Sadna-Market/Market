package main.System.Server.Domain.StoreModel;

public class Inventory {


    public boolean isProductExistInStock(int productId, int quantity){
        return false;
    }

    public boolean addNewProduct(int productId, String productName, String categori, double price, int quantity, String description) {
        return false;
    }

    public boolean removeProduct(int productId) {
        return false;
    }

    public boolean setProduct(int productId, String productName, String category, int price, int quantity, String description) {
        return false;
    }
}
