package main.System.Server.Domain.StoreModel;

import org.apache.log4j.Logger;

import java.util.List;

public class History {
    private final int TID;
    private final double finalPrice;
    private final List<ProductStore> products;
    private final String user;

    static Logger logger=Logger.getLogger(History.class);

    public History(int TID, double finalPrice, List<ProductStore> products, String user){
        this.TID = TID;
        this.finalPrice = finalPrice;
        this.products = products;
        this.user = user;
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
