package com.example.demo.Service.ServiceObj;

import com.example.demo.Domain.Market.DetailsPurchase;

import java.util.List;

public class ServiceDetailsPurchase {
    public int TID;
    public double finalPrice;
    public List<Integer> boughtInStores;

    public ServiceDetailsPurchase(DetailsPurchase d){
        this.TID = d.getTID();
        this.finalPrice = d.getFinalPrice();
        this.boughtInStores = d.getBoughtInStores();
    }

}
