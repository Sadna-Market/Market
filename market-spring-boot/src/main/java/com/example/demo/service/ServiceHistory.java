package com.example.demo.service;


import java.util.List;

public class ServiceHistory {
    private final int TID;
    private final double finalPrice;
    private final List<ServiceProductStore> products;
    private final String user;

    public ServiceHistory(int tid, double finalPrice, List<ServiceProductStore> products, String user) {
        TID = tid;
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
