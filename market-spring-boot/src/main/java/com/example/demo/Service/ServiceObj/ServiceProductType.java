package com.example.demo.Service.ServiceObj;

import com.example.demo.Domain.Market.ProductType;

import java.util.ArrayList;
import java.util.List;

public class ServiceProductType {

    public int productID;
    public int rate;
    public String productName;
    public String description;
    public int category;
    public List<Integer> stores;

    public ServiceProductType(ProductType productType){
        this.productID = productType.getProductID().value;
        this.rate = productType.getRate().value;
        this.productName = productType.getProductName().value;
        this.description = productType.getDescription().value;
        this.category = productType.getCategory().value;
        this.stores = new ArrayList<>();
        stores.addAll(productType.getStores().value);
    }
}
