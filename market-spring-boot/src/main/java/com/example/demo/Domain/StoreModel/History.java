package com.example.demo.Domain.StoreModel;

import com.example.demo.DataAccess.Entity.DataHistory;
import com.example.demo.DataAccess.Entity.DataProductStore;
import com.example.demo.DataAccess.Entity.DataProductStoreHistory;
import com.example.demo.Domain.Market.ProductType;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class History {
    private int TID;
    private int supplyID;
    private double finalPrice;
    private List<ProductStore> products;
    private String user;

    static Logger logger = Logger.getLogger(History.class);

    public History(int TID, int supplyID, double finalPrice, List<ProductStore> products, String user) {
        this.TID = TID;
        this.supplyID = supplyID;
        this.finalPrice = finalPrice;
        this.products = products;
        this.user = user;
    }

    public void setProducts(List<ProductStore> products) {
        this.products = products;
    }

    public History() {
    }

    public History(Integer TID, Integer supplyID, Double finalPrice, String user) {
        this.TID = TID;
        this.supplyID = supplyID;
        this.finalPrice = finalPrice;
        this.user = user;
        List<ProductStore> products=new LinkedList<>();

    }

    public int getTID() {
        return TID;
    }

    public int getSupplyID() {
        return supplyID;
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

    public DataHistory getDataObject() {
        DataHistory dataHistory = new DataHistory();
        dataHistory.setTID(TID);
        dataHistory.setFinalPrice(this.finalPrice);
        dataHistory.setSupplyId(this.supplyID);
        dataHistory.setUser(user);
        return dataHistory;
    }

    public History fromData(DataHistory dhistory) {
        this.TID = dhistory.getHistoryId();
        this.supplyID = dhistory.getSupplyId();
        this.finalPrice = dhistory.getFinalPrice();
        this.products = dhistory.getProducts()
                .stream()
                .map(dataProductStoreHistory -> {
                    var productType = new ProductType().fromData(dataProductStoreHistory.getProductType());
                    return new ProductStore(productType,
                            dataProductStoreHistory.getQuantity(),
                            dataProductStoreHistory.getPrice());
                }).collect(Collectors.toList());
        return this;
    }
}
