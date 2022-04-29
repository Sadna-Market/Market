package main.Service;

import main.System.Server.Domain.StoreModel.History;
import main.System.Server.Domain.StoreModel.ProductStore;

import java.util.List;

public class ServiceHistory {
    private final int TID;
    private final double finalPrice;
    private final List<ProductStore> products;
    private final String user;

    public ServiceHistory(History history){
        this.TID = history.getTID();
        this.finalPrice = history.getFinalPrice();
        this.products = history.getProducts();
        this.user = history.getUser();
    }
    public int getTID() {
        return TID;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<ProductStore> getProducts() {
        return products;
    }

    public String getUser() {
        return user;
    }
}
