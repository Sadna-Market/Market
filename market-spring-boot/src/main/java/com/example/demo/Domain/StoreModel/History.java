package com.example.demo.Domain.StoreModel;

import com.example.demo.Service.ServiceObj.ServiceProductStore;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class History {
    private final int TID;
    private final int supplyID;
    private final double finalPrice;
    private final List<ServiceProductStore> products;
    private final String user;

    static Logger logger=Logger.getLogger(History.class);

    public History(int TID, int supplyID, double finalPrice, List<ProductStore> products, String user){
        this.TID = TID;
        this.supplyID = supplyID;
        this.finalPrice = finalPrice;
        this.products = new ArrayList<>();
        for(ProductStore p: products){
            this.products.add(new ServiceProductStore(p));
        }
        this.user = user;
    }


    public int getTID() {
        return TID;
    }

    public int getSupplyID() { return supplyID; }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<ServiceProductStore> getProducts() {
        return products;
    }

    public String getUser() {
        return user;
    }
}
