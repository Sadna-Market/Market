package com.example.demo.Service.ServiceObj;


import com.example.demo.Domain.StoreModel.History;

import java.util.ArrayList;
import java.util.List;

public class ServiceHistory {
    public final int TID;
    public final double finalPrice;
    public final List<ServiceProductStore> products;
    public final String user;

    public ServiceHistory(History history){
        this.TID = history.getTID();
        this.finalPrice = history.getFinalPrice();
        this.products = new ArrayList<>();
        history.getProducts().forEach(productStore -> {
            this.products.add(new ServiceProductStore(productStore));
        });
        this.user = history.getUser();
    }

    public int getTID() {
        return TID;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<ServiceProductStore> getProducts() {
        return products;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "ServiceHistory{" +
                "TID=" + TID +
                ", finalPrice=" + finalPrice +
                ", products=" + products +
                ", user='" + user + '\'' +
                '}';
    }
}
