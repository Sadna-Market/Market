package com.example.demo.Domain.Market;

import java.util.List;

public class DetailsPurchase {
    private int TID;
    private double finalPrice;
    private List<Integer> boughtInStores;

    public DetailsPurchase(int TID, double finalPrice, List<Integer> stores){
        this.TID = TID;
        this.finalPrice = finalPrice;
        this.boughtInStores = stores;
    }

    public int getTID() {
        return TID;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public List<Integer> getBoughtInStores() {
        return boughtInStores;
    }

}
