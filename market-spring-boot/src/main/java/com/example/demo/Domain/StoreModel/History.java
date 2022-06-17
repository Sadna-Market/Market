package com.example.demo.Domain.StoreModel;

import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataProductStore;
import com.example.demo.Service.ServiceObj.ServiceProductStore;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class History {
    private final int TID;
    private final int supplyID;
    private final double finalPrice;
    private final List<ProductStore> products;
    private final String user;

    static Logger logger=Logger.getLogger(History.class);

    public History(int TID, int supplyID, double finalPrice, List<ProductStore> products, String user){
        this.TID = TID;
        this.supplyID = supplyID;
        this.finalPrice = finalPrice;
        this.products = products;
        this.user = user;
    }


    public int getTID() {
        return TID;
    }

    public int getSupplyID() { return supplyID; }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<ProductStore> getProducts() {
        return products;
    }

    public String getUser() {
        return user;
    }

    public DataHistory getDataObject() {
        DataHistory dataHistory = new DataHistory();
        dataHistory.setHistoryId(this.TID);
        dataHistory.setFinalPrice(this.finalPrice);
        dataHistory.setSupplyId(this.supplyID);
        dataHistory.setUser(this.user);
        Set<DataProductStore> productStores = products.stream().map(ProductStore::getDataObject).collect(Collectors.toSet());
        dataHistory.setProducts(productStores);
        return dataHistory;
    }
}
